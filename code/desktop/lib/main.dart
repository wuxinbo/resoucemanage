import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'ui/photopage.dart';
import 'ui/navigation.dart';

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
  late Photopage photopage;

  @override
  Widget build(BuildContext context) {
    photopage = Photopage();
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

            // SizedBox(
            //   width: 30,
            // ),
            // Expanded(
            //   child: photopage,
            // ),
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
