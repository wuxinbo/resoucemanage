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
using Poco::Net::TCPServer;
using Poco::Net::TCPServerConnection;
using Poco::Net::TCPServerConnectionFactory;
using Poco::Net::TCPServerConnectionFactoryImpl;
using Poco::Net::TCPServerConnectionFilter;

FormattingChannel *pFCConsole = nullptr;

// 保存客户端连接
std::map<std::string, StreamSocket> clientSocketMap;

NET_NAMESPACE_START
JNIEnv *jnienv;
class ClientConnection : public TCPServerConnection
{
private:
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
                if (jnienv)
                {
                    /* code */
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
                Logger::formatDump(msg, buffer, n);
                

                n = ss.receiveBytes(buffer, sizeof(buffer));
            }
            // 移除
            clientSocketMap.erase(sstream.str());
        }
        catch (Exception &exc)
        {
            std::cerr << "ClientConnection: " << exc.displayText() << std::endl;
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

JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_startServer(JNIEnv *env, jclass, jint jport)
{

    try
    {
        xbwuc_net::jnienv =env;
        pFCConsole = new FormattingChannel(new PatternFormatter("[%O] %s: %p: %t"));
        pFCConsole->setChannel(new ConsoleChannel);
        pFCConsole->open();
        Poco::UInt16 port = jport;
        Logger::create("ConsoleLogger", pFCConsole);
        TCPServer srv(new NET::TCPFactory(), port);
        srv.start();
        std::cout << "TCP server listening on port " << port << '.'
                  << std::endl;

        NET::terminator.wait();
        std::cout << "server shutdown" << std::endl;
    }
    catch (Exception &exc)
    {
        std::cerr << exc.displayText() << std::endl;
        return;
    }
}

int main()
{
    try
    {
        pFCConsole = new FormattingChannel(new PatternFormatter("[%O] %s: %p: %t"));
        pFCConsole->setChannel(new ConsoleChannel);
        pFCConsole->open();
        Poco::UInt16 port = NumberParser::parse("8080");
        Logger::create("ConsoleLogger", pFCConsole);
        TCPServer srv(new NET::TCPFactory(), port);
        srv.start();
        std::cout << "TCP server listening on port " << port << '.'
                  << std::endl
                  << "Press Ctrl-C to quit." << std::endl;

        NET::terminator.wait();
        std::cout << "server shutdown" << std::endl;
    }
    catch (Exception &exc)
    {
        std::cerr << exc.displayText() << std::endl;
        return 1;
    }
}
