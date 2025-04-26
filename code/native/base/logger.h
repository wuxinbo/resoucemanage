//
// 日志输出封装
//

#ifndef RESOURCE_LOGGER_H
#define RESOURCE_LOGGER_H


#define LOG_INFO_DATA2(tag,fmt,data1,data2)  xbwuc::Logger::info(__FILE__,__LINE__,tag, fmt, data1,data2);
#define LOG_INFO_DATA(tag,fmt,data1)  xbwuc::Logger::info(__FILE__,__LINE__,tag, fmt, data1);
#define LOG_INFO(tag,fmt)  xbwuc::Logger::info(__FILE__,__LINE__,tag, fmt, "");
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
    static void info(const char *  fileName,int linenumber, const char * tag,std::string fmt,std::string data... );

};

XBWUC_NAMESPACE_END

#endif //RESOURCE_LOGGER_H
