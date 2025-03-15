import 'package:flutter/material.dart';
import 'photopage.dart';

/**
 * 首页左侧导航栏
 */
class NavRail extends StatefulWidget {
  const NavRail({super.key});

  @override
  State<NavRail> createState() => _NavRailState();
}

/**
 * 首页左侧导航栏状态管理
 */
class _NavRailState extends State<NavRail> {
  int _selectedIndex = 0;

  Widget curentWidgt() {
    switch (_selectedIndex) {
      //按照拍摄日期显示照片
      case 0:
        return Row(children: [
          const SizedBox(
            width: 20,
          ), //左侧边距
          Expanded(
            child: Photopage(),
          ),
        ]);
      case 1:
        return Text('selectedIndex123: $_selectedIndex');
      case 2:
        return Text('selectedIndex123: $_selectedIndex');
      default:
        return Text('selectedIndex123: $_selectedIndex');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: <Widget>[
          NavigationRail(
            selectedIndex: _selectedIndex,
            onDestinationSelected: (int index) {
              setState(() {
                _selectedIndex = index;
              });
            },
            labelType: NavigationRailLabelType.selected,
            destinations: const <NavigationRailDestination>[
              NavigationRailDestination(
                icon: Icon(Icons.favorite_border),
                selectedIcon: Icon(Icons.favorite_border),
                label: Text('照片'),
              ),
              NavigationRailDestination(
                icon: Icon(Icons.favorite_border),
                selectedIcon: Icon(Icons.favorite),
                label: Text('喜欢'),
              ),
              NavigationRailDestination(
                icon: Icon(Icons.pie_chart_outline_rounded),
                selectedIcon: Icon(Icons.pie_chart_rounded),
                label: Text('统计'),
              ),
            ],
          ),
          const VerticalDivider(thickness: 1, width: 1),
          // This is the main content.
          Expanded(
            child: Center(
              child: curentWidgt(),
            ),
          )
        ],
      ),
    );
  }
}
