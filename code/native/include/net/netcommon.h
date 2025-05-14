
#pragma once
#include <iostream>
#include <stdint.h>
// 取消内存对齐
#pragma pack(push, 1)
#include "common.h"
#include "logger.h"
#include <functional>
#include <string>

NET_NAMESPACE_START
/**
 * 数据类型
 */
struct DataType {
  /**
   * UTF-8 字符串
   */
  static const uint8_t STRING = 1;
  /**
   * 文件
   */
  static const uint8_t FILE = 2;
  /**
   * 二进制数据
   */
  static const uint8_t BINARY = 3;
};

/**
 * 协议头
 */
struct Message {
  // 魔数
  uint32_t magicNumber = 0xBEBEBEBE;
  //  版本
  uint8_t version = 1;
  /**
   * 数据类型
   * */
  uint8_t type = DataType::STRING;
  // body 长度
  uint32_t length = 0;
  /**
   * 数据
   */
  char data[0];
};

/**
 *  数据解析
 */
inline void parseData(Message *message,
                      std::function<void(std::string&)> dataReceiveFunc) {
  Message defaultMessage;
  // 如果一致在进行解析
  if (message->magicNumber == defaultMessage.magicNumber) {
    if (message->type == DataType::STRING) { // utf8 字符串
      // 如果是jni 调用则反过来调用java 方法将收到的数据进行回传
      std::string str(message->data, message->length);
      LOG_INFO_DATA("message: %s", str);
      if (dataReceiveFunc) {
        dataReceiveFunc(str);
      }
      // 由于多条消息会拼接在一起，所以需要判断消息是否结束
      char *p = (char *)message;
      Message *newMessage = (Message *)(p + sizeof(Message) + message->length);
      if (newMessage && newMessage->length > 0) {
        parseData(newMessage, dataReceiveFunc);
      }
    }
  }
}

NET_NAMESPACE_END
#pragma pack(pop)
