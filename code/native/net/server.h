
#include "net/netcommon.h"
#include "common.h"
NET_NAMESPACE_START
 class TCPServer {
public:
 TCPServer();

 ~TCPServer();

void setTcpClass(jclass tcpClientClass);

	/**
	* 启动服务器
	*/
 void start(int port);


};
NET_NAMESPACE_END