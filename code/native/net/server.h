

#include "net/netcommon.h"
#include "common.h"
NET_NAMESPACE_START
 class TCPServer {
public:
XBWUC_NET_API TCPServer();

XBWUC_NET_API ~TCPServer();

void setTcpClass(jclass tcpClientClass);

	/**
	* Æô¶¯tcp ·þÎñ
	*/
XBWUC_NET_API void start(int port);


};
NET_NAMESPACE_END