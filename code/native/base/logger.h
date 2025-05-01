//
// 日志输出封装
//

#ifndef RESOURCE_LOGGER_H
#define RESOURCE_LOGGER_H

#if defined(_MSC_VER)
    #define FUNC_NAME __FUNCSIG__
#elif defined(__GNUC__) || defined(__clang__)
    #define FUNC_NAME __PRETTY_FUNCTION__
#else
    #define FUNC_NAME __func__
#endif
#define LOG_INFO_DATA2(fmt,data1,data2)  xbwuc::Logger::info(__FILE__,__LINE__,FUNC_NAME, fmt, data1,data2);
#define LOG_INFO_DATA(fmt,data1)  xbwuc::Logger::info(__FILE__,__LINE__,FUNC_NAME, fmt, data1,"");
#define LOG_INFO(fmt) xbwuc::Logger::info(__FILE__,__LINE__,FUNC_NAME, fmt, "","");
#include "common.h"
#include <string>
XBWUC_NAMESPACE_START
class LoggerImpl;
// class
class Logger {
private:

    LoggerImpl * loggerImpl;
public:
    // 初始化
    Logger();
    ~Logger();
    // 日志输出
    static void info(const char *  fileName,int linenumber,
         const char * tag,std::string fmt,
         std::string data,std::string data2... );

};

XBWUC_NAMESPACE_END

#endif //RESOURCE_LOGGER_H
