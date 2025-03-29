
#pragma once
#include <stdint.h>
// 取消内存对齐
#pragma pack(push,1)

/**
 * 数据类型
 */
 struct DataType
{
    /**
     * UTF-8 字符串
     */
    static const uint8_t STRING =1;
    /**
     * 文件
     */
    static const uint8_t FILE=2;
    /**
     * 二进制数据
     */
    static const uint8_t BINARY=3;

};
/**
 * 协议头
 */
struct Message
{
    // 魔数
    uint32_t  magicNumber=0xBEBEBEBE;
    //  版本
    uint8_t version =1;
    /**
     * 数据类型 
     * */ 
    uint8_t type=DataType::STRING;
    // body 长度
    uint32_t length = 0;
    /**
     * 数据
     */
    char data[0];
};
#pragma pack(pop)
#define NET xbwuc_net
#define NET_NAMESPACE_START namespace xbwuc_net {

#define NET_NAMESPACE_END };



