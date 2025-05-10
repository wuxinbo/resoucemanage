#pragma once

#include "net/netcommon.h"
#include "common.h"
#include <functional>
#include <string>
NET_NAMESPACE_START
class TcpClientImpl;
class TCPClient
{

public:
    int init();
    //注册数据处理函数
    void registerDataReceiveFunc(std::function<void(std::string)> func);
    void connect(const char * addr);
    /**
     * 发送utf-8 data
     */
     int sendUTFData(const char *addr,const char *data);
};

NET_NAMESPACE_END
