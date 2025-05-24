#include "include/core/core_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "core_plugin.h"

void CorePluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
        // hello
  core::CorePlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
          
}
