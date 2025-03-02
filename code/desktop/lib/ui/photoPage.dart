import 'dart:convert';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import '../net/Http.dart';
import 'package:intl/intl.dart';

class PhotoInfoDateWidget extends StatelessWidget {
  /**
   * 时间
   */
  String date;
  List<dynamic> photos;
  PhotoInfoDateWidget(this.date, this.photos);
  //默认一行显示6个
  int gridCloum = 0;
  double imageWidth = 200;
  double imageHeight = 200;

  Widget bottomImageInfo(dynamic photo) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        const SizedBox(height: 30),
        //光圈
        Text(" ${photo['aperture']}",
            style: const TextStyle(fontSize: 12, color: Colors.grey)),
        const SizedBox(width: 10),
        Text(" ${photo['speed']}",
            style: const TextStyle(fontSize: 12, color: Colors.grey)),
        const SizedBox(width: 10),
        Text(" ${photo['focusLength']}",
            style: const TextStyle(fontSize: 12, color: Colors.grey)),
      ],
    );
  }

  /**
   * 封装图片控件
   */
  Widget imageView(dynamic photo) {
    var image = CachedNetworkImage(
      imageUrl: Request.PHOTO_IMAGE_URL + photo['mid'].toString(),
      fit: BoxFit.cover,
      height: imageHeight,
      width: imageWidth,
      errorWidget: (context, url, error) => const Icon(Icons.error),
    );
    return Column(
      children: [
        Expanded(child: image, flex: 1),
        bottomImageInfo(photo),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    // 构建图片网格
    final screenWidth = MediaQuery.of(context).size.width;
    // 根据窗口宽度计算每行显示的数量
    gridCloum = (screenWidth / imageWidth).floor();
    var view = GridView.count(
      crossAxisCount: gridCloum,
      crossAxisSpacing: 5,
      mainAxisSpacing: 8,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      children: photos.map<Widget>((photo) {
        return imageView(photo);
      }).toList(),
    );
    return Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      //顶部边距
      const SizedBox(
        height: 0,
      ),
      // 日期信息
      Text(
        '$date ${photos.length}张',
        style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
      ),
      //边距
      const SizedBox(
        height: 20,
      ),
      // 图片往右偏移20
      Row(children: [
        const SizedBox(
          width: 20,
        ), //左侧边距
        Expanded(
          child: view,
        ),
      ]),
    ]);
  }
}

class Photopage extends StatefulWidget {
  Photopage() {}
  late PhotoPageState state;
  @override
  State<StatefulWidget> createState() {
    state = PhotoPageState();
    return state;
  }

  void refresh() {
    PhotoPageState().getList();
  }
}

class PhotoPageState extends State<Photopage> {
  var shotDates = <PhotoInfoDateWidget>[];
  /**
   * 按天进行分组
   */
  Map<String, List<dynamic>> photoByDate = Map();
  @override
  void initState() {
    super.initState();
    getList();
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: shotDates),
    );
  }

  /**
   * 获取照片列表
   */
  void getList() {
    Request.queryPhotoByPage((response) {
      var photoList = jsonDecode(response.body)['content'];
      photoByDate = Map();
      for (var i = 0; i < photoList.length; i++) {
        var photo = photoList[i];
        if (photo != null) {
          var shotTime = photo['shotTime']?.substring(0, 10);
          //去重重复的数据，按照日期进行分组
          String key =
              DateFormat('yyyy年MM月dd日').format(DateTime.parse(shotTime));
          if (photoByDate[key] == null) {
            photoByDate[key] = [];
          }
          photoByDate[key]?.add(photo);
        }
      }
      setState(() {
        shotDates = <PhotoInfoDateWidget>[];
        //显示去除的日期
        photoByDate.forEach((key, value) {
          shotDates.add(PhotoInfoDateWidget(key, value));
        });
      });
    });
  }
}
