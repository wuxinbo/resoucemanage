

#include "Poco/Net/StreamSocket.h"
#include "Poco/Net/SocketAddress.h"
#include <iostream>
#include <thread>
#include <string>
#include "net/common.h"
#include "client.h"
#include <map>
#include <memory>
#include "jni/TCPServerAndClient.h"
#include "jni/comon.h"
using Poco::Net::SocketAddress;
using Poco::Net::StreamSocket;
using std::string;

/**
 * java 数据发送接口
 */
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_TCPClient_sendUTFData(
    JNIEnv * env,
    jobject object , jstring jaddr, jstring jdata)
{
    jboolean iscopy =false;
    if(jstringNullCheck(env,jaddr)!=0){
        std::cout<< "addr is null"<< std::endl; 
        return ;
    }
    if(jstringNullCheck(env,jdata)!=0){
        std::cout<< "jdata is null"<< std::endl; 
        return ;
    }
    LPCSTR addr = env ->GetStringUTFChars(jaddr,&iscopy);
    LPCSTR data = env ->GetStringUTFChars(jdata,&iscopy);
    NET::TcpClient client;
    client.sendUTFData(addr,data);
}

NET_NAMESPACE_START
class TcpClientImpl
{
private:
    // 客户端
    static std::map<string, std::shared_ptr<StreamSocket>> clientMap;

public:
    static void initSock(const char *addr)
    {
        SocketAddress sa(addr);
        put(addr, std::make_shared<StreamSocket>(sa));
    }
    static std::shared_ptr<StreamSocket> get(const char *addr)
    {
        std::string key(addr);
        auto it = clientMap.find(key);
        if (it != clientMap.end())
        {                      // 正确检查迭代器是否有效
            return it->second; // 通过迭代器访问 shared_ptr 并获取原始指针
        }
        else
        {
            initSock(addr);
            return get(addr);
        }
        return nullptr;
    }
    static void put(const char *addr, std::shared_ptr<StreamSocket> ptr)
    {
        std::string key(addr);
        clientMap.insert({key, ptr});
    }
};

int TcpClient::sendUTFData(const char *addr, const char *data)
{
    Message message;
    message.length = strlen(data);
    int size = sizeof(message);
    std::unique_ptr buffer = std::make_unique<char[]>(sizeof(message) + message.length);
    // 消息头复制
    memcpy(buffer.get(), &message, sizeof(message));
    memccpy(buffer.get() + sizeof(message), data, 0, message.length);
    TcpClientImpl::get(addr).get()->sendBytes(buffer.get(), sizeof(message) + message.length);
    return 0;
}

NET_NAMESPACE_END
std::map<string, std::shared_ptr<StreamSocket>> NET::TcpClientImpl::clientMap;
