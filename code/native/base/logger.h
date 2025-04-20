//
// 日志输出封装
//
#include "common.h"
#ifndef RESOURCE_LOGGER_H
#define RESOURCE_LOGGER_H
#include <string>
XBWUC_NAMESPACE_START
class LoggerImpl;
// class
class Logger {
private:
    LoggerImpl * loggerImpl;
public:
    Logger();
    ~Logger();
    // 日志输出
    static void info(const char * tag,std::string fmt,std::string data... );

};

XBWUC_NAMESPACE_END

#endif //RESOURCE_LOGGER_H
