//
// tcpclient.cpp
//
// This sample demonstrates the StreamSocket and SocketStream classes.
//
// Copyright (c) 2005-2006, Applied Informatics Software Engineering GmbH.
// and Contributors.
//
// SPDX-License-Identifier:	BSL-1.0
//

#include "Poco/Net/StreamSocket.h"
#include "Poco/Net/SocketAddress.h"
#include "Poco/Exception.h"
#include <iostream>
#include <thread>
#include <string>
#include "net/common.h"
#include "client.h"
#include <map>
#include <memory>
using Poco::Exception;
using Poco::Net::SocketAddress;
using Poco::Net::StreamSocket;
using std::string;

void receiveData(StreamSocket sock)
{
    std::cout << "start receive data is " << std::endl;
    char data[254];
    int size = sock.receiveBytes(data, sizeof(data));
    if (size > 0)
    {
        std::string msg;
        std::cout << "receive data is " << data << std::endl;
    }
}
// /**
//  * 字符串发送
//  */
// int sendUtfData(char* addr, char* data){

// }


NET_NAMESPACE_START
class TcpClientImpl
{
private:
    // 客户端
    static std::map<string, std::shared_ptr<StreamSocket>> clientMap;
public:

    static void initSock(const char * addr){
        SocketAddress sa(addr);
        put(addr,std::make_shared<StreamSocket>(sa));
    }
    static std::shared_ptr<StreamSocket> get(const char *addr)
    {
        std::string key(addr);
        auto it = clientMap.find(key);
        if (it != clientMap.end())
        {                            // 正确检查迭代器是否有效
            return it->second; // 通过迭代器访问 shared_ptr 并获取原始指针
        }else{
            initSock(addr);
            return get(addr);
        }
        return nullptr;
    }
    static void put(const char *addr, std::shared_ptr<StreamSocket> ptr){
        std::string key(addr);
        clientMap.insert({key,ptr});
    }
};

int TcpClient::sendUTFData(const char *addr,const char *data)
{
    Message message;
    message.length = strlen(data);
    int size =sizeof(message);
    std::unique_ptr buffer =std::make_unique<char[]>(sizeof(message)+message.length);
    //消息头复制
    memcpy(buffer.get(),&message,sizeof(message));
    memccpy(buffer.get()+sizeof(message),data,0,message.length);
    TcpClientImpl::get(addr).get()->sendBytes(buffer.get(),sizeof(message)+message.length);
    return 0;
}

NET_NAMESPACE_END
std::map<string, std::shared_ptr<StreamSocket>> NET::TcpClientImpl::clientMap;

