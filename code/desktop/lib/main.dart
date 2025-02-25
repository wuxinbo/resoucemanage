import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'ui/photopage.dart';

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
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  late Photopage photopage;

  @override
  Widget build(BuildContext context) {
    photopage = Photopage();
    return Scaffold(
      body: Center(
        child: Column(
          children: <Widget>[
            Expanded(
              child: photopage,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: photopage.refresh,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
