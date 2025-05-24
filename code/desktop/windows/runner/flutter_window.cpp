#include "flutter_window.h"

#include <iostream>
#include <memory>
#include <optional>
#include <windef.h>
#include "flutter/generated_plugin_registrant.h"
#include "core/core_plugin_c_api.h"
#include "flutter/encodable_value.h"
#include <flutter/event_channel.h>
#include <flutter/event_stream_handler.h>
#include <flutter/event_stream_handler_functions.h>
#include <flutter/standard_method_codec.h>
// #include "logger.h"
const char *eventType = "eventType";
std::shared_ptr<flutter::EventChannel<flutter::EncodableValue>> event_channel;
std::unique_ptr<flutter::EventSink<flutter::EncodableValue>> eventSink;

FlutterWindow::FlutterWindow(const flutter::DartProject& project)
    : project_(project) {}

FlutterWindow::~FlutterWindow() {}
/**
* 初始化eventchanal
*/
void initEventChannel(std::shared_ptr<flutter::FlutterViewController> flutter_controller_) {
   // 传输数据的通道
  event_channel =
      std::make_shared<flutter::EventChannel<flutter::EncodableValue>>(
          flutter_controller_->engine()->messenger(), "data_event",
          &flutter::StandardMethodCodec::GetInstance());
  // 创建streamhandler
  auto streamhandler = std::make_unique<flutter::StreamHandlerFunctions<>>(
      [](const flutter::EncodableValue *arguments,
         std::unique_ptr<flutter::EventSink<flutter::EncodableValue>> &&events)
          -> std::unique_ptr<
              flutter::StreamHandlerError<flutter::EncodableValue>> {
        eventSink = std::move(events);
        return nullptr;
      },
      [](const flutter::EncodableValue *arguments)
          -> std::unique_ptr<
              flutter::StreamHandlerError<flutter::EncodableValue>> {
        return nullptr;
      });
  event_channel->SetStreamHandler(std::move(streamhandler));
}

bool FlutterWindow::OnCreate() {
  if (!Win32Window::OnCreate()) {
    return false;
  }

  RECT frame = GetClientArea();

  // The size here must match the window dimensions to avoid unnecessary surface
  // creation / destruction in the startup path.
  flutter_controller_ = std::make_shared<flutter::FlutterViewController>(
      frame.right - frame.left, frame.bottom - frame.top, project_);
  // Ensure that basic setup of the controller was successful.
  if (!flutter_controller_->engine() || !flutter_controller_->view()) {
    return false;
  }
  RegisterPlugins(flutter_controller_->engine());

  SetChildContent(flutter_controller_->view()->GetNativeWindow());
   
  flutter_controller_->engine()->SetNextFrameCallback([&]() {
    this->Show();
  });

  // Flutter can complete the first frame before the "show window" callback is
  // registered. The following call ensures a frame is pending to ensure the
  // window is shown. It is a no-op if the first frame hasn't completed yet.
  flutter_controller_->ForceRedraw();
  initEventChannel(flutter_controller_);
  return true;
}

void FlutterWindow::OnDestroy() {
  if (flutter_controller_) {
    flutter_controller_ = nullptr;
  }

  Win32Window::OnDestroy();
}

LRESULT
FlutterWindow::MessageHandler(HWND hwnd, UINT const message,
                              WPARAM const wparam,
                              LPARAM const lparam) noexcept {
  // Give Flutter, including plugins, an opportunity to handle window messages.
  if (flutter_controller_) {
    std::optional<LRESULT> result =
        flutter_controller_->HandleTopLevelWindowProc(hwnd, message, wparam,
                                                      lparam);
    if (result) {
      return *result;
    }
  }
  switch (message) {
    case WM_FONTCHANGE:
      flutter_controller_->engine()->ReloadSystemFonts();
      break;
    case WM_DATA_RECEIVE: {
      // 处理数据接收消息
      auto data = reinterpret_cast<char*>(lparam);
      std ::cout << "data receive:" << data << std::endl;
      flutter::EncodableMap data_map{
          {flutter::EncodableValue(eventType),
           flutter::EncodableValue("tcpDataReceive")},
          {flutter::EncodableValue("data"), flutter::EncodableValue(data)}};
      if(eventSink){
        eventSink->Success(flutter::EncodableValue(data_map));
      }      

         break;
    }
  }

  return Win32Window::MessageHandler(hwnd, message, wparam, lparam);
}
