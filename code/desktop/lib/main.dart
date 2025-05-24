import 'dart:convert';
import 'dart:isolate';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'ui/navigation.dart';
import 'package:core/core.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
  debugPaintSizeEnabled = false;
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '资源管理',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  EventChannel eventChannel = const EventChannel("data_event");
  @override
  Widget build(BuildContext context) {
    eventChannel.receiveBroadcastStream().listen((event) {
      print("收到服务端发来的消息：${event}");
    }, onError: (error) {
      print("error: $error");
    });
    Core().connect("192.168.2.3:8082");
    var height = MediaQuery.of(context).size.height;
    var width = MediaQuery.of(context).size.width;
    return Scaffold(
      body: Center(
        child: Row(
          children: <Widget>[
            ConstrainedBox(
              constraints: BoxConstraints(maxHeight: height, maxWidth: width),
              child: NavRail(),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: null,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
