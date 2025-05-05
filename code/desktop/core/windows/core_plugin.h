#ifndef FLUTTER_PLUGIN_CORE_PLUGIN_H_
#define FLUTTER_PLUGIN_CORE_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace core {

class CorePlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  CorePlugin();

  virtual ~CorePlugin();

  // Disallow copy and assign.
  CorePlugin(const CorePlugin&) = delete;
  CorePlugin& operator=(const CorePlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace core

#endif  // FLUTTER_PLUGIN_CORE_PLUGIN_H_
