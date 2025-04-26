//
// Created by wuxinbo on 2025/4/19.
//
#include "logger.h"
#include "Poco/FileChannel.h"
#ifdef ANDROID
#include <android/log.h>
#endif
#include "Poco/ConsoleChannel.h"
#include "Poco/Format.h"
#include "Poco/FormattingChannel.h"
#include "Poco/Logger.h"
#include "Poco/PatternFormatter.h"
#include <iostream>

using Poco::ConsoleChannel;
using Poco::FormattingChannel;
using Poco::PatternFormatter;

FormattingChannel *pFCConsole;

xbwuc::Logger::Logger() {}

void xbwuc::Logger::info(const char *fileName, int linenumber, const char *tag,
                         std::string fmt, std::string data...) {
#ifndef ANDROID
  if (!pFCConsole) {
    pFCConsole = new FormattingChannel(
        new PatternFormatter("[%O(%u)] %Y-%m-%d %H:%M:%S.%i  %p: %t"));
     
    pFCConsole->setChannel(new ConsoleChannel);
    pFCConsole->open();
    Poco::Logger::create("ConsoleLogger", pFCConsole);
  }

#endif
#ifdef ANDROID
  __android_log_print(android_LogPriority::ANDROID_LOG_INFO, tag,
                      Poco::format(fmt, data).c_str(), data[0]);
  return;
#endif

  Poco::Logger &consoleLogger = Poco::Logger::get("ConsoleLogger");
  consoleLogger.information(
      Poco::format(Poco::format("[%s]  %s", std::string(tag), fmt), data),
      fileName, linenumber);
}
