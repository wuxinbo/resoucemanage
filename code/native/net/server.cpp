#include <iostream>
#include "Poco/Net/TCPServer.h"
#include "Poco/Net/TCPServerConnection.h"
#include "Poco/Net/TCPServerConnectionFactory.h"
#include "Poco/Net/StreamSocket.h"
#include "Poco/NumberParser.h"
#include "Poco/Logger.h"
#include "Poco/Process.h"
#include "Poco/Message.h"
#include "Poco/NamedEvent.h"
#include "net/common.h"
#include "Poco/FormattingChannel.h"
#include "Poco/ConsoleChannel.h"
#include "Poco/PatternFormatter.h"
#include <map>
#include <sstream>
#include "jni/TCPServerAndClient.h"
#include "Poco/NumberFormatter.h"
#include "jni/comon.h"
#include <mutex>
#include "server.h"
using Poco::ConsoleChannel;
using Poco::Event;
using Poco::Exception;
using Poco::FormattingChannel;
using Poco::Logger;
using Poco::NamedEvent;
using Poco::NumberParser;
using Poco::PatternFormatter;
using Poco::UInt16;
using Poco::Net::StreamSocket;
using Poco::Net::TCPServerConnection;
using Poco::Net::TCPServerConnectionFactory;
using Poco::Net::TCPServerConnectionFactoryImpl;
using Poco::Net::TCPServerConnectionFilter;

#ifdef WIN32
using Poco::Process;
using Poco::Net::SocketAddress;
using Poco::ProcessImpl;
#endif // 


FormattingChannel *pFCConsole = nullptr;

// 保存客户端连接
std::map<std::string, StreamSocket> clientSocketMap;
NET_NAMESPACE_START
class ClientConnection : public TCPServerConnection
{
private:


    /**
     * 将数据回传给Java
     * 
     */
    void invokeJavaRecive(std::string & data){
        std::lock_guard<std::mutex> lock( getJniMutex());;
        JNIEnv* jnienv =nullptr;
        JavaVM* jvm = getjvm();
        jint res  =jvm->AttachCurrentThread((void**)&jnienv,nullptr);
        res =jvm->GetEnv((void**) &jnienv,JNI_VERSION_1_8);
        if(!jnienv){
            std::cout<< "get env fail" <<std::endl;
            return ;
        }
        jclass tcpClass =jnienv->FindClass("com/wuxinbo/resourcemanage/jni/TCPServerClient");
        jmethodID method= jnienv->GetStaticMethodID(tcpClass,"receiveData","(Ljava/lang/String;)V");
        if (method == nullptr) {
            std::cout<< "method is null" <<std::endl;
            // 打印异常信息
            jnienv->ExceptionDescribe();
            jnienv->ExceptionClear();
            return; // 或处理错误
        }
        jstring jstr = jnienv->NewStringUTF(data.c_str());
        jnienv->CallStaticVoidMethod(tcpClass,method,jstr);
        jvm->DetachCurrentThread();
    }
    /**
     *  数据解析
     */
    void parseData(Message *message)
    {
        Message defaultMessage ;
        //如果一致在进行解析
        if (message->magicNumber== defaultMessage.magicNumber)
        {
            if(message->type ==DataType::STRING){ //utf8 字符串
                //如果是jni 调用则反过来调用java 方法将收到的数据进行回传
                Logger &consoleLogger = Logger::get("ConsoleLogger");
                std::string str(message->data,message->length);
                consoleLogger.information("content is %s", str);
                if (getjvm())
                {
                    invokeJavaRecive(str);
                }
                
            }
        }
        

    }

public:
    ClientConnection(const StreamSocket &s) : TCPServerConnection(s)
    {
    }

    void run()
    {
        StreamSocket &ss = socket();
        auto address = ss.peerAddress();
        Logger &consoleLogger = Logger::get("ConsoleLogger");
        std::ostringstream sstream;
        sstream << address.host().toString() << ":" << address.port();
        //
        clientSocketMap.insert({sstream.str(), ss});
        consoleLogger.information(Poco::format("address is %s create socket ,  current client is %s",
                                               sstream.str(), Poco::NumberFormatter::format(clientSocketMap.size())));
        try
        {
            char buffer[1024 * 20] = {0};
            Message *message = (Message *)buffer;
            int n = ss.receiveBytes(buffer, sizeof(buffer));
            while (n > 0)
            {

                std::cout << "收到" << n << " bytes, data length is  " << message->length << std::endl;
                std::string msg(buffer, n);
                parseData(message);
                // Logger::formatDump(msg, buffer, n);
                n = ss.receiveBytes(buffer, sizeof(buffer));
            }
            // 移除
            clientSocketMap.erase(sstream.str());
        }
        catch (Exception &exc)
        {
            clientSocketMap.erase(sstream.str());
            consoleLogger.information( "ClientConnection: %s",exc.displayText().c_str());
        }
    }
};

typedef TCPServerConnectionFactoryImpl<ClientConnection> TCPFactory;
#if defined(POCO_OS_FAMILY_WINDOWS)
NamedEvent terminator(ProcessImpl::terminationEventName(Process::id()));
#else
Event terminator;
#endif
}

XBWUC_NET_API void NET::TCPServer::start(int port)
{
	try
	{
		pFCConsole = new FormattingChannel(new PatternFormatter("[%O] %s: %p: %t"));
		pFCConsole->setChannel(new ConsoleChannel);
		pFCConsole->open();
		Logger::create("ConsoleLogger", pFCConsole);
		Poco::Net::TCPServer srv(new NET::TCPFactory(), port);
		srv.start();
		std::cout << "TCP server listening on port " << port << '.'
			<< std::endl
			<< "Press Ctrl-C to quit." << std::endl;
        terminator.wait();
        std::cout << "shutdown server" << std::endl;
    
	}
	catch (Exception& exc)
	{
		std::cout << exc.displayText() << std::endl;
	}
}

XBWUC_NET_API NET::TCPServer::TCPServer()
{
}
XBWUC_NET_API NET::TCPServer::~TCPServer()
{
}

void invokeJavaRecive(char * data,JNIEnv* jnienv){
    getJniMutex();
    // JNIEnv* jnienv =nullptr;
    JavaVM* jvm = getjvm();
    std::cout<< "jvm "<< jvm <<std::endl;

    // jvm->AttachCurrentThread((void**) jnienv,nullptr);
    // if(!jnienv){
    //     std::cout<< "get env fail" <<std::endl;
    //     return ;
    // }
    // getjvm()->GetEnv((void**) jnienv,JNI_VERSION_1_8);
    // if (!jnienv)
    // {
    //     std::cout<< "get env fail" <<std::endl;
    // }
    jclass tcpClass =jnienv->FindClass("com/wuxinbo/resourcemanage/jni/TCPServerClient");
    jmethodID method= jnienv->GetStaticMethodID(tcpClass,"receiveData","(Ljava/lang/String;)V");
    jstring jstr = jnienv->NewStringUTF(data);
    jnienv->CallStaticVoidMethod(tcpClass,method,jstr);
    std::cout<< "data send " <<std::endl;

}

