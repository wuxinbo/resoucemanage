
#include "resource_manage_ImageMagick.h"
#include <iostream>
#include "MagickWand/MagickWand.h"
#include <string.h>
#ifdef WIN32
#include <windows.h>
#endif
#include <mutex>
#include <vector>
#include "jni/comon.h"
std::mutex magick_wands_mutex;
const int MAX_SIZE = 10;
std::vector<MagickWand *> magick_wands;
/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_ImageMagick_init(JNIEnv *, jclass)
{
    MagickWandGenesis();
    for (int i = 0; i < MAX_SIZE; i++)
    {
        magick_wands.emplace_back(NewMagickWand());
    }
}

// 参数检查
inline int checkParam(JNIEnv *env, jstring srcPath,
                      jstring destPath)
{
    if (jstringNullCheck(env, srcPath) != 0)
    {
        std::cout << "srcPath is null" << std::endl;
        return -1;
    }
    if (jstringNullCheck(env, destPath) != 0)
    {
        std::cout << "destPath is null" << std::endl;
        return -1;
    }
    
    return 0;
}

MagickWand* get(){

    std::lock_guard<std::mutex> lock(magick_wands_mutex);
    if(!magick_wands.empty()){ //不为空则获取最后一个返回
        MagickWand* wand = magick_wands.back();
        magick_wands.pop_back();
        return wand;
    }else{
        MagickWand* wand = NewMagickWand();
        magick_wands.emplace_back(wand);
        return wand;
    }
}

void put(MagickWand* wand){
    std::lock_guard<std::mutex> lock(magick_wands_mutex);
    if(magick_wands.size() < MAX_SIZE){
        magick_wands.emplace_back(wand);
    }else{ //资源销毁
        DestroyMagickWand(wand);
    }
}

/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    resize
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_wuxinbo_resourcemanage_jni_ImageMagick_resize(JNIEnv *env, jclass, jstring srcPath,
                                                                              jstring destPath, jint scale)
{
    jboolean iscopy = false;
    MagickBooleanType status;
    if (magick_wands.size() == 0)
    {
        std::cout << "init fail" << std::endl;
        return -1;
    }

    if (checkParam(env, srcPath, destPath) != 0)
    {
        return -1;
    }
    LPCSTR srcPathStr = env->GetStringUTFChars(srcPath, &iscopy);
    if (srcPathStr == NULL || strlen(srcPathStr) == 0)
    {
        std::cout << "srcPath is null" << std::endl;
        return -1;
    }
    std::cout << "srcPath is " << srcPathStr << std::endl;
    LPCSTR destPathStr = env->GetStringUTFChars(destPath, &iscopy);
    if (destPathStr == NULL || strlen(destPathStr) == 0)
    {
        std::cout << "destPath is null" << std::endl;
        return -1;
    }
    std::cout << "destPathStr is " << destPathStr << std::endl;
    MagickWand *magick_wand = get();
    status = MagickReadImage(magick_wand, srcPathStr);
    if (status == MagickFalse)
    {
        std::cout << "Read image failed" << std::endl;
        return -1;
    }
    int scaleValue = scale > 0 ? scale : 10;
    int height = MagickGetImageHeight(magick_wand);
    int width = MagickGetImageWidth(magick_wand);
    std::cout << "Height: " << height << " Width: " << width << std::endl;
    MagickResetIterator(magick_wand);
    MagickResizeImage(magick_wand, width / scaleValue, height / scaleValue, LanczosFilter);
    status = MagickWriteImages(magick_wand, destPathStr, MagickTrue);
    // 清除图片序列，否则每次调用会产生多余的图片
    ClearMagickWand(magick_wand);
    // 释放字符串
    env->ReleaseStringUTFChars(srcPath, srcPathStr);
    env->ReleaseStringUTFChars(destPath, destPathStr);
    put(magick_wand);
    return 0;
}

/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_ImageMagick_destroy(JNIEnv *, jclass)
{
    for (int i = 0; i < MAX_SIZE; i++)
    {
        DestroyMagickWand(magick_wands[i]);
    }
    magick_wands.clear();
    MagickWandTerminus();
}
