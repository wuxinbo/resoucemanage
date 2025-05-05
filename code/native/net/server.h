
#include "net/netcommon.h"
#include "common.h"
NET_NAMESPACE_START
 class TCPServer {
public:
 TCPServer();

 ~TCPServer();

 /**
 * 
 * 发送数据
  * @param adress 地址
 * @param data 数据
 *
 */
 void sendUTFData(const char * adress,const char * data);
	/**
	* 启动服务器
	*/
 void start(int port);


};
NET_NAMESPACE_END