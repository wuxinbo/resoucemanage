#pragma once

#include "net/common.h"

NET_NAMESPACE_START
class TcpClientImpl;
class TcpClient
{

public:
    int init();
    /**
     * 发送utf-8 data
     */
    int sendUTFData(const char *addr,const char *data);
};

NET_NAMESPACE_END
