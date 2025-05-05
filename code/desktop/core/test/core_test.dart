import 'package:flutter_test/flutter_test.dart';
import 'package:core/core.dart';
import 'package:core/core_platform_interface.dart';
import 'package:core/core_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockCorePlatform
    with MockPlatformInterfaceMixin
    implements CorePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final CorePlatform initialPlatform = CorePlatform.instance;

  test('$MethodChannelCore is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelCore>());
  });

  test('getPlatformVersion', () async {
    Core corePlugin = Core();
    MockCorePlatform fakePlatform = MockCorePlatform();
    CorePlatform.instance = fakePlatform;

    expect(await corePlugin.getPlatformVersion(), '42');
  });
}
