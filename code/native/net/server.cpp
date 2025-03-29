#include <iostream>
#include "Poco/Net/TCPServer.h"
#include "Poco/Net/TCPServerConnection.h"
#include "Poco/Net/TCPServerConnectionFactory.h"
#include "Poco/Net/StreamSocket.h"
#include "Poco/NumberParser.h"
#include "Poco/Logger.h"
#include "Poco/Process.h"
#include "Poco/NamedEvent.h"
#include "net/common.h"
using Poco::Event;
using Poco::Exception;
using Poco::Logger;
using Poco::NamedEvent;
using Poco::NumberParser;
using Poco::Process;
using Poco::ProcessImpl;
using Poco::UInt16;
using Poco::Net::StreamSocket;
using Poco::Net::TCPServer;
using Poco::Net::TCPServerConnection;
using Poco::Net::TCPServerConnectionFactory;
using Poco::Net::TCPServerConnectionFactoryImpl;
using Poco::Net::TCPServerConnectionFilter;




namespace
{
    class ClientConnection : public TCPServerConnection
    {
    public:
        ClientConnection(const StreamSocket &s) : TCPServerConnection(s)
        {
        }

        void run()
        {
            StreamSocket &ss = socket();
            try
            {
                char buffer[1024*20]={0};
                Message* message =(Message* )buffer;
                int n = ss.receiveBytes(buffer, sizeof(buffer));
                while (n > 0)
                {
                    std::cout << "收到" << n << " bytes, data length is  "<< message->length << std::endl;
                    std::string msg(buffer,n);
                    Logger::formatDump(msg, buffer, n);
                    std::cout << "" << msg << std::endl;
                    n = ss.receiveBytes(buffer, sizeof(buffer));
                }
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
int main()
{
    try
    {
        Poco::UInt16 port = NumberParser::parse("8080");

        TCPServer srv(new TCPFactory(), port);
        srv.start();

        std::cout << "TCP server listening on port " << port << '.'
                  << std::endl
                  << "Press Ctrl-C to quit." << std::endl;

        terminator.wait();
    }
    catch (Exception &exc)
    {
        std::cerr << exc.displayText() << std::endl;
        return 1;
    }
    std::cout << "hello,world" << std::endl;
}
