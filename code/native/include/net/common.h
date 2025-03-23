

#include <stdint.h>
// 取消内存对齐
#pragma pack(1)

/**
 * 数据类型
 */
enum class DataType: uint8_t
{
    /**
     * UTF-8 字符串
     */
    STRING =0,
    /**
     * 文件
     */
    FILE=1,
    /**
     * 二进制数据
     */
    BINARY=2

};
/**
 * 协议头
 */
struct Header
{
    // 魔数
    uint32_t  magicNumber=0xBEBEBEBE;
    //  版本
    uint8_t version =1;
    /**
     * 数据类型 
     * */ 
    DataType type= DataType::STRING;
};
