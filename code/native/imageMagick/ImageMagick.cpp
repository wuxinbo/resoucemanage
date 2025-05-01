

#include "Poco/NumberFormatter.h"
#include "jni.h"
#include "resource_manage_ImageMagick.h"
#include <cstddef>
#include <minwindef.h>
#include <mutex>
#include <string.h>

#ifdef WIN32
#include <windows.h>
#endif
#include "imageMagick.h"
#include "jni/comon.h"
#include "logger.h"
#include <vector>
#define MAGICK_TAG __FUNCSIG__

std::vector<MagickWand *> magick_wands;
static const int MAX_SIZE = 10;

void xbwuc::ImageMagick::initMaickWand() {
    LOG_INFO( "开始加锁");
    std::unique_lock<std::mutex> lock(getJniMutex());
    LOG_INFO( "加锁完成");
  
    MagickWandGenesis();
    for (int i = 0; i < MAX_SIZE; i++) {
      magick_wands.emplace_back(NewMagickWand());
    }
    LOG_INFO_DATA("init magickwand size is %s",
                  INT_FORMAT(magick_wands.size()))
  }
void xbwuc::ImageMagick::put(MagickWand *wand) {
  std::lock_guard<std::mutex> lock(getJniMutex());
  if (magick_wands.size() < MAX_SIZE) {
    magick_wands.emplace_back(wand);
  } else { // 资源销毁
    DestroyMagickWand(wand);
  }
}

MagickWand* xbwuc::ImageMagick::get() {
  char * data = nullptr;
  std::lock_guard<std::mutex> lock(getJniMutex());
  if (!magick_wands.empty()) { // 不为空则获取最后一个返回
    MagickWand *wand = magick_wands.back();
    magick_wands.pop_back();
    return wand;
  } else {
    MagickWand *wand = NewMagickWand();
    magick_wands.emplace_back(wand);
    LOG_INFO("create new magick wand");
    return wand;
  }
}

int xbwuc::ImageMagick::resize(const char *srcPathStr, const char *destPathStr,
                               int scale) {
  MagickWand *magick_wand = get();
  int status = MagickReadImage(magick_wand, srcPathStr);
  if (status == MagickFalse) {
    LOG_INFO( "read image fail");
    return -1;
  }
  int scaleValue = scale > 0 ? scale : 10;
  int height = MagickGetImageHeight(magick_wand);
  int width = MagickGetImageWidth(magick_wand);

  LOG_INFO_DATA2("width is %s height is %s",
                 INT_FORMAT(width).c_str(), INT_FORMAT(height).c_str());
  MagickResetIterator(magick_wand);
  MagickResizeImage(magick_wand, width / scaleValue, height / scaleValue,
                    LanczosFilter);
  status = MagickWriteImages(magick_wand, destPathStr, MagickTrue);
  // 清除图片序列，否则每次调用会产生多余的图片
  ClearMagickWand(magick_wand);
  put(magick_wand);
  return 0;
}

void xbwuc::ImageMagick::destroy() {
  std::lock_guard<std::mutex> lock(getJniMutex());
  for (int i = 0; i < magick_wands.size(); i++) {
    DestroyMagickWand(magick_wands[i]);
  }
  magick_wands.clear();
}

/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    init
 * Signature: ()V
 */

JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_ImageMagick_init(JNIEnv *, jclass) {
  xbwuc::ImageMagick::initMaickWand();
  LOG_INFO( "init success");
}

// 参数检查
inline int checkParam(JNIEnv *env, jstring srcPath, jstring destPath) {
  if (jstringNullCheck(env, srcPath) != 0) {
    LOG_INFO(  "srcPath is null");
    return -1;
  }
  if (jstringNullCheck(env, destPath) != 0) {
    LOG_INFO(  "destPath is null");
    return -1;
  }

  return 0;
}

/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    resize
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_wuxinbo_resourcemanage_jni_ImageMagick_resize(
    JNIEnv *env, jclass, jstring srcPath, jstring destPath, jint scale) {
  jboolean iscopy = false;
  MagickBooleanType status;
  if (magick_wands.size() == 0) {
    LOG_INFO( "init fail");
    return -1;
  }

  if (checkParam(env, srcPath, destPath) != 0) {
    return -1;
  }
  const char *srcPathStr = env->GetStringUTFChars(srcPath, &iscopy);
  if (srcPathStr == NULL || strlen(srcPathStr) == 0) {
    LOG_INFO( "srcPath is null");
    return -1;
  }
  LOG_INFO_DATA("srcPath is %s", srcPathStr);
  const char *destPathStr = env->GetStringUTFChars(destPath, &iscopy);
  if (destPathStr == NULL || strlen(destPathStr) == 0) {
    LOG_INFO( "destoath is null");
    return -1;
  }
  LOG_INFO_DATA("destPath is %s", destPathStr);
  xbwuc::ImageMagick::resize(srcPathStr, destPathStr, scale);
  LOG_INFO( "完成图片压缩");
  // 释放字符串
  env->ReleaseStringUTFChars(srcPath, srcPathStr);
  env->ReleaseStringUTFChars(destPath, destPathStr);
  return 0;
}

/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_ImageMagick_destroy(JNIEnv *, jclass) {
  xbwuc::ImageMagick::destroy();
}
