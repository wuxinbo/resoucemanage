import 'core_platform_interface.dart';
import 'package:flutter/services.dart';
import 'core_method_channel.dart';

class Core {
  Future<String?> getPlatformVersion() {
    return CorePlatform.instance.getPlatformVersion();
  }

  Future<void> connect(String address) async {
    MethodChannel methodCall = CorePlatform.instance.getMethodChannel();
    return await CorePlatform.instance.connect(address);
  }
}
