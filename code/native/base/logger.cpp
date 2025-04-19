//
// Created by wuxinbo on 2025/4/19.
//
#include "logger.h"
#include <android/log.h>
#include "Poco/Format.h"
#include "Poco/Logger.h"
#include "Poco/ConsoleChannel.h"
#include "Poco/FormattingChannel.h"
#include "Poco/PatternFormatter.h"
#include "Poco/NumberFormatter.h"
#include "Poco/ConsoleChannel.h"

using Poco::FormattingChannel;
using Poco::PatternFormatter;
using Poco::ConsoleChannel;

FormattingChannel *pFCConsole;

xbwuc::Logger::Logger() {
#ifndef ANDROID
    if(pFCConsole) return;
    pFCConsole = new FormattingChannel( new PatternFormatter("[%O] %s: %p: %t"));
    pFCConsole->setChannel(new ConsoleChannel);
    pFCConsole->open();
    Poco::Logger::create("ConsoleLogger", pFCConsole);
#endif
}

void xbwuc::Logger::info(const char *tag, std::string fmt, std::string data...) {

#ifdef  ANDROID
    __android_log_print(android_LogPriority::ANDROID_LOG_INFO, tag, Poco::format(fmt, data).c_str(),
                        data[0]);
    return;
#endif

    Poco::Logger &consoleLogger = Poco::Logger::get("ConsoleLogger");
    consoleLogger.information(Poco::format(fmt, data).c_str(), "");

}

