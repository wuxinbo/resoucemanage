#include "server.h"
#include "Poco/Logger.h"
#include "Poco/NamedEvent.h"
#include "Poco/Net/StreamSocket.h"
#include "Poco/Net/TCPServer.h"
#include "Poco/Net/TCPServerConnection.h"
#include "Poco/Net/TCPServerConnectionFactory.h"
#include "Poco/NumberFormatter.h"
#include "Poco/Process.h"
#include "jni/comon.h"
#include "net/netcommon.h"
#include <exception>
#include <iostream>
#include <map>
#include <mutex>
#include <sstream>

using Poco::Event;
using Poco::Exception;
using Poco::NamedEvent;
using Poco::UInt16;
using Poco::Net::StreamSocket;
using Poco::Net::TCPServerConnection;
using Poco::Net::TCPServerConnectionFactoryImpl;

#ifdef WIN32
using Poco::Process;
using Poco::ProcessImpl;
using Poco::Net::SocketAddress;
#endif //

#include "logger.h"

using FormatintFunc = std::string (*)(int);

FormatintFunc intFormat = &Poco::NumberFormatter::format;
// 保存客户端连接
std::map<std::string, StreamSocket> clientSocketMap;

NET_NAMESPACE_START
/**
* 数据处理函数
*/
    std::function<void(std::string)> dataReceiveFunc;
/**
 * 客户端连接移除函数
 */
    std::function<void(std::string)> clientRemoveFunc;

/**
* 客户端连接
*/
    std::function<void(std::string)> clientConnectFunc;


    class ClientConnection : public TCPServerConnection {
    private:

    public:
        ClientConnection(const StreamSocket &s) : TCPServerConnection(s) {}

        void run() {
            StreamSocket &ss = socket();
            auto address = ss.peerAddress();
            std::ostringstream sstream;
            sstream << address.host().toString() << ":" << address.port();
            //
            clientSocketMap.insert({sstream.str(), ss});
            LOG_INFO(Poco::format("address is %s create socket ,  current client is %s",
                                  sstream.str(), intFormat(clientSocketMap.size())));
            if (clientConnectFunc) {
                try {
                    clientConnectFunc(sstream.str());
                } catch (std::exception &e) {
                    LOG_INFO_DATA("client connect func error %s", e.what());
                }

            }
            try {
                char buffer[1024 * 20] = {0};
                Message *message = (Message *) buffer;
                int n = ss.receiveBytes(buffer, sizeof(buffer));
                while (n > 0) {
                    std::string msg(buffer, n);
                    parseData(message, dataReceiveFunc);
                    n = ss.receiveBytes(buffer, sizeof(buffer));
                }
                // 移除
                clientSocketMap.erase(sstream.str());
                if (clientRemoveFunc) clientRemoveFunc(sstream.str());
                LOG_INFO_DATA("%s disconnect ", sstream.str());
            } catch (Exception &exc) {
                clientSocketMap.erase(sstream.str());
            }
        }
    };

    typedef TCPServerConnectionFactoryImpl <ClientConnection> TCPFactory;
#if defined(POCO_OS_FAMILY_WINDOWS)
    NamedEvent terminator(ProcessImpl::terminationEventName(Process::id()));
#else
    Event terminator;
#endif
}

void NET::TCPServer::start(int port) {
    try {
        Poco::Net::TCPServer srv(new NET::TCPFactory(), port);
        srv.start();
        LOG_INFO_DATA("TCP server listening on port %s ",
                      Poco::NumberFormatter::format(port));
        terminator.wait();

    } catch (Exception &exc) {
        std::cout << exc.displayText() << std::endl;
    }
}

/**
 * 注册数据接收函数
 * @param func 数据接收函数
 */
void NET::TCPServer::registerDataReceiveFunc(std::function<void(std::string)> func) {
    dataReceiveFunc = func;
}

void NET::TCPServer::registerClientConnectFunc(std::function<void(std::string)> func) {
    clientConnectFunc = func;
}


void NET::TCPServer::registerClientRemoveFunc(std::function<void(std::string)> func) {
    clientRemoveFunc = func;
}

/**
 *  发送数据到客户端
 *  @param address 客户端地址 ip:port
 *  @param data 发送的数据
 */
void NET::TCPServer::sendUTFData(const char *address, const char *data) {
    auto it = clientSocketMap.find(address);
    if (it != clientSocketMap.end()) {
        StreamSocket &socket = it->second;
        Message message;
        message.length = strlen(data);
        int size = sizeof(message);
        std::unique_ptr buffer = std::make_unique<char[]>(sizeof(message) + message.length);
        // 消息头复制
        memcpy(buffer.get(), &message, sizeof(message));
        memcpy(buffer.get() + sizeof(message), data, message.length);
        socket.sendBytes(buffer.get(), sizeof(message) + message.length);
        LOG_INFO_DATA("send data to %s", address);
    } else {
        LOG_INFO_DATA("client %s not found", address);
    }
}

NET::TCPServer::TCPServer() {}

NET::TCPServer::~TCPServer() {}
