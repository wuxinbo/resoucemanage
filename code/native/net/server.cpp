#include <iostream>
#include "Poco/Net/TCPServer.h"
#include "Poco/Net/TCPServerConnection.h"
#include "Poco/Net/TCPServerConnectionFactory.h"
#include "Poco/Net/StreamSocket.h"
#include "Poco/Logger.h"
#include "Poco/Process.h"
#include "Poco/NamedEvent.h"
#include "net/netcommon.h"
#include "Poco/NumberFormatter.h"
#include <map>
#include <sstream>
#include "jni/TCPServerAndClient.h"
#include "jni/comon.h"
#include <mutex>
#include "server.h"
using Poco::Event;
using Poco::Exception;
using Poco::NamedEvent;
using Poco::UInt16;
using Poco::Net::StreamSocket;
using Poco::Net::TCPServerConnection;
using Poco::Net::TCPServerConnectionFactoryImpl;

#ifdef WIN32
using Poco::Process;
using Poco::Net::SocketAddress;
using Poco::ProcessImpl;
#endif // 
#include "logger.h"
using FormatintFunc = std::string (*)(int);

FormatintFunc  intFormat = &Poco::NumberFormatter::format ;
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
        jint version=JNI_VERSION_1_6;
        #ifdef ANDROID
         jint res  =jvm->AttachCurrentThread(&jnienv,nullptr);
         version = JNI_VERSION_1_6;
        #else
        jint res  =jvm->AttachCurrentThread((void**)&jnienv,nullptr);
        #endif
        #ifdef WIN32
          version=JNI_VERSION_1_8;
        #endif
        jvm->GetEnv((void**)&jnienv,version);
        if(!jnienv){
            std::cout<< "get env fail" <<std::endl;
            return ;
        }
        jclass tcpClass =getTcpClass();
        if (!tcpClass) {
            LOG_INFO("tcpClientClass is null ");
            return ;
        }
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
                std::string str(message->data,message->length);
                LOG_INFO_DATA( "message: %s",str);
                LOG_INFO_DATA("data length is %s",intFormat(message->length) );
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
        std::ostringstream sstream;
        sstream << address.host().toString() << ":" << address.port();
        //
        clientSocketMap.insert({sstream.str(), ss});
        LOG_INFO(Poco::format("address is %s create socket ,  current client is %s",sstream.str(),
                            intFormat(clientSocketMap.size())));
        try
        {
            char buffer[1024 * 20] = {0};
            Message *message = (Message *)buffer;
            int n = ss.receiveBytes(buffer, sizeof(buffer));
            while (n > 0)
            {

                std::string msg(buffer, n);
                parseData(message);
                n = ss.receiveBytes(buffer, sizeof(buffer));
            }
            // 移除
            clientSocketMap.erase(sstream.str());
        }
        catch (Exception &exc)
        {
            clientSocketMap.erase(sstream.str());
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
 void NET::TCPServer::start(int port)
{
	try
	{
		Poco::Net::TCPServer srv(new NET::TCPFactory() , port);
		srv.start();
        LOG_INFO_DATA("TCP server listening on port %s ",Poco::NumberFormatter::format(port));
        terminator.wait();

	}
	catch (Exception& exc)
	{
		std::cout << exc.displayText() << std::endl;
	}
}

 NET::TCPServer::TCPServer()
{
}
 NET::TCPServer::~TCPServer()
{
}


