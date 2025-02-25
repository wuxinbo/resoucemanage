import 'package:http/http.dart' as http;
import 'package:http/src/response.dart';

/**
 * 网络请求
 */
class Request {
  // 基础路径
  static const String baseUrl = '192.168.2.3:8081';

  static const String PHOTO_LIST_BY_PAGE = "photo/listByPage";
  // 图片访问地址
  static const String PHOTO_IMAGE_URL = "http://" + baseUrl + '/photo/get?mid=';
  static Future<Response> get(String url) async {
    var fullUrl = Uri.http(baseUrl, url);
    var response = await http.get(fullUrl);
    print('Response status: ${response.statusCode}');
    print('Response body: ${response.body}');
    return response;
  }

  /**
   * 查询图片信息
   */
  static Future<void> queryPhotoByPage(Function callback) async {
    var response = await get(PHOTO_LIST_BY_PAGE);
    callback(response);
  }

  static Future<void> post() async {
    var url = Uri.http(baseUrl, 'whatsit/create');
    var response =
        await http.post(url, body: {'name': 'doodle', 'color': 'blue'});
    print('Response status: ${response.statusCode}');
    print('Response body: ${response.body}');
  }
}
