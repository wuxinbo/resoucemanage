
#include "net/netcommon.h"
#include "common.h"
#include <functional>
#include <string>
NET_NAMESPACE_START
class TCPServer {

public:
 TCPServer();

 ~TCPServer();

 //注册数据处理函数
 void registerDataReceiveFunc(std::function<void(std::string)> func);
 //注册客户端连接函数
 void registerClientConnectFunc(std::function<void(std::string)> func);

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