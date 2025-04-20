#pragma once

#include "net/netcommon.h"
#include "common.h"
NET_NAMESPACE_START
class TcpClientImpl;
class TCPClient
{

public:
    int init();

    void connect(const char * addr);
    /**
     * 发送utf-8 data
     */
    XBWUC_NET_API int sendUTFData(const char *addr,const char *data);
};

NET_NAMESPACE_END
