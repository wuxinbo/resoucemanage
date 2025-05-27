
#include "client.h"
#include "Poco/Net/SocketAddress.h"
#include "Poco/Net/StreamSocket.h"
#include "jni/TCPServerAndClient.h"
#include "jni/comon.h"
#include "logger.h"
#include "net/netcommon.h"
#include <iostream>
#include <map>
#include <memory>
#include <mutex>
#include <string>
#include <thread>

using Poco::Net::SocketAddress;
using Poco::Net::StreamSocket;
using std::string;

NET_NAMESPACE_START

std::function<void(std::string&)> clientDataReceiveFunc;
std::mutex clientMutex;
class TcpClientImpl {
private:
  // 客户端
  static std::map<string, std::shared_ptr<StreamSocket>> clientMap;

public:
  static void initSock(const char *addr) {
    SocketAddress sa(addr);
    put(addr, std::make_shared<StreamSocket>(sa));
  }
  static std::shared_ptr<StreamSocket> get(const char *addr) {
    // std::lock_guard<std::mutex> lock(clientMutex);
    std::string key(addr);
    auto it = clientMap.find(key);
    if (it != clientMap.end()) {
      // 判断连接是否有效
      StreamSocket *socket = it->second.get();
      if (socket->impl()->getError()) {
        std::cout << "socket error" << socket->impl()->getError() << std::endl;
      }
      // 正确检查迭代器是否有效
      return it->second; // 通过迭代器访问 shared_ptr 并获取原始指针
    } else {
      initSock(addr);
      return get(addr);
    }
    return nullptr;
  }
  static void put(const char *addr, std::shared_ptr<StreamSocket> ptr) {
    std::string key(addr);
    clientMap.insert({key, ptr});
  }
};

int TCPClient::sendUTFData(const char *addr, const char *data) {
  Message message;
  message.length = strlen(data);
  int size = sizeof(message);
  std::unique_ptr buffer =
      std::make_unique<char[]>(sizeof(message) + message.length);
  // 消息头复制
  memcpy(buffer.get(), &message, sizeof(message));
  // 数据复制
  memccpy(buffer.get() + sizeof(message), data, 0, message.length);
  TcpClientImpl::get(addr).get()->sendBytes(buffer.get(),
                                            sizeof(message) + message.length);
  return 0;
}
void TCPClient::registerDataReceiveFunc(std::function<void(std::string&)> func) {
  clientDataReceiveFunc = func;
}

void TCPClient::connect(const char *addr) {
  // 建立连接
  std::string serverAddr(addr);
  std::thread clientThread([serverAddr] {
    try {
      auto socket = TcpClientImpl::get(serverAddr.c_str());
      char buffer[1024] = {0};
      Message *message = (Message *)buffer;
      int n = socket.get()->receiveBytes(buffer, sizeof(buffer));
      while (n>0) {
        parseData(message, clientDataReceiveFunc);
        LOG_INFO("continue receive data ");
        // buffer[1024] = {0};
        n = socket.get()->receiveBytes(buffer, sizeof(buffer));
      }
      LOG_INFO("receive data done");
    } catch (std::exception &e) {
      LOG_INFO_DATA("connec server fail:%s", e.what());
    }
  });
  clientThread.detach();
}

NET_NAMESPACE_END
std::map<string, std::shared_ptr<StreamSocket>> NET::TcpClientImpl::clientMap;
