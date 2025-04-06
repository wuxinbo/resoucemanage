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



FormattingChannel *pFCConsole=nullptr;
NET_NAMESPACE_START
class ClientConnection : public TCPServerConnection
{
private:

public:
    ClientConnection(const StreamSocket &s) : TCPServerConnection(s)
    {
   
    }

    void run()
    {
        StreamSocket &ss = socket();
        auto address = ss.peerAddress();
        Logger& consoleLogger = Logger::get("ConsoleLogger");
        std::cout<< "connect address " << address.host().toString().c_str()<< ":"<< address.port()<<std::endl;
        consoleLogger.information("address is %s", address.host().toString().c_str());
        try
        {
            char buffer[1024 * 20] = {0};
            Message *message = (Message *)buffer;
            int n = ss.receiveBytes(buffer, sizeof(buffer));
            while (n > 0)
            {
                std::cout << "收到" << n << " bytes, data length is  " << message->length << std::endl;
                std::string msg(buffer, n);
                Logger::formatDump(msg, buffer, n);
                consoleLogger.information("content is %s", msg);

                n = ss.receiveBytes(buffer, sizeof(buffer));
            }
            // ss.
            consoleLogger.information("shutdown");
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
