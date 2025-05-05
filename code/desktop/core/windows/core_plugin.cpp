#include "core_plugin.h"

// This must be included before many other Windows headers.
#include <windows.h>

// For getPlatformVersion; remove unless needed for your plugin implementation.
#include <VersionHelpers.h>

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>
#include <flutter/standard_method_codec.h>

#include <memory>
#include <sstream>
#include "client.h"
#include "logger.h"
namespace core {

// static
void CorePlugin::RegisterWithRegistrar(
    flutter::PluginRegistrarWindows *registrar) {
  auto channel =
      std::make_unique<flutter::MethodChannel<flutter::EncodableValue>>(
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
    std::ostringstream version_stream;
    version_stream << "Windows ";
    if (IsWindows10OrGreater()) {
      version_stream << "10+";
    } else if (IsWindows8OrGreater()) {
      version_stream << "8";
    } else if (IsWindows7OrGreater()) {
      version_stream << "7";
    }
    result->Success(flutter::EncodableValue(version_stream.str()));
  }else if(method_call.method_name().compare("connect") == 0){
      NET::TCPClient client;
      const flutter::EncodableValue* arg = method_call.arguments();
      int index = arg->index();
      std::string adress = std::get<std::string>(*arg);
      if (!adress.c_str()) {
		  LOG_INFO("adress is null");
          return;
      }
	  LOG_INFO_DATA("connect server address is ", adress.c_str());
      client.connect(adress.c_str());
  }
  else {
      result->NotImplemented();
  }
}

}  // namespace core
