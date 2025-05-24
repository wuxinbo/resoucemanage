#include "core_plugin.h"
#include <cstring>
#include <iostream>
#include <windows.h>
#include <VersionHelpers.h>
#include "client.h"
#include "flutter/encodable_value.h"
#include "logger.h"
#include "include/core/core_plugin_c_api.h"
#include <flutter/event_stream_handler.h>
#include <flutter/event_stream_handler_functions.h>
#include <flutter/method_channel.h>
#include <flutter/event_channel.h>
#include <flutter/plugin_registrar_windows.h>
#include <flutter_messenger.h>
#include <flutter/standard_method_codec.h>
#include <memory>
#include <winnt.h>
#include <winuser.h>
namespace core {

std::shared_ptr<flutter::MethodChannel<flutter::EncodableValue>> channel;
// 父窗口句柄


// static
void CorePlugin::RegisterWithRegistrar(
    flutter::PluginRegistrarWindows *registrar) {
  channel = std::make_shared<flutter::MethodChannel<flutter::EncodableValue>>(
      registrar->messenger(), "core",
      &flutter::StandardMethodCodec::GetInstance());
  
  auto plugin = std::make_unique<CorePlugin>();

  channel->SetMethodCallHandler(
      [plugin_pointer = plugin.get()](const auto &call, auto result) {
        plugin_pointer->HandleMethodCall(call, std::move(result));
      });

  registrar->AddPlugin(std::move(plugin));
}

CorePlugin::CorePlugin() {}

CorePlugin::~CorePlugin() {}

void CorePlugin::HandleMethodCall(
    const flutter::MethodCall<flutter::EncodableValue> &method_call,
    std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result) {
  if (method_call.method_name().compare("getPlatformVersion") == 0) {
    result->Success(flutter::EncodableValue("windows 10"));
  } else if (method_call.method_name().compare("connect") == 0) {
    NET::TCPClient client;
    const flutter::EncodableValue *arg = method_call.arguments();
    std::string adress = std::get<std::string>(*arg);
    if (!adress.c_str()) {
      LOG_INFO("adress is null");
      return;
    }
    // 注册数据接收回调函数
    client.registerDataReceiveFunc([&](std::string& data) -> void {
        // eventSink->Success(flutter::EncodableValue(data));
    HWND pwindow = FindWindow(L"FLUTTER_RUNNER_WIN32_WINDOW",L"resource");
    if (!pwindow) return; 
    //  auto dataPtr =std::make_shared<char[]>(strlen(data.c_str()));
     bool result = SendMessage(pwindow, WM_DATA_RECEIVE,
         0,reinterpret_cast<LPARAM>(data.c_str()));
    });
    client.connect(adress.c_str());
  } else {
    result->NotImplemented();
  }
}

} // namespace core
