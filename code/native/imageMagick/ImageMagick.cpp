
#include "resource_manage_ImageMagick.h"
#include <iostream>
#include "MagickWand/MagickWand.h"
#include <string.h>
#include <windows.h>
MagickWand *magick_wand;
/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_ImageMagick_init(JNIEnv *, jclass)
{
    MagickWandGenesis();
    magick_wand = NewMagickWand();
}


inline char* convertjStringToChar(JNIEnv * env, jstring jstr)
{
    jboolean iscopy = false;
    LPCSTR str = env->GetStringUTFChars(jstr, &iscopy);
    return (char*)str;
}
/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    resize
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_wuxinbo_resourcemanage_jni_ImageMagick_resize(JNIEnv * env, jclass, jstring srcPath,
                                                                              jstring destPath, jint scale)
{
    jboolean iscopy = false;
    MagickBooleanType status;
    if (magick_wand == NULL)
    {
        // init fail
        std::cout << "init fail" << std::endl;
        return -1;
    }
    LPCSTR srcPathStr = env->GetStringUTFChars(srcPath, &iscopy);
    if(srcPathStr == NULL||strlen(srcPathStr)==0){
        std::cout << "srcPath is null" << std::endl;
        return -1;
    }
    LPCSTR destPathStr =env->GetStringUTFChars(destPath, &iscopy);
    if(destPath == NULL||strlen(destPathStr)==0){
        std::cout << "destPath is null" << std::endl;
        return -1;
    }
    status = MagickReadImage(magick_wand, srcPathStr);
    if (status == MagickFalse)
    {
        std::cout << "Read image failed" << std::endl;
        exit(0);
    }
    int scaleValue =scale>0?scale: 10;
    int height = MagickGetImageHeight(magick_wand);
    int width = MagickGetImageWidth(magick_wand);
    std::cout << "Height: " << height << " Width: " << width << std::endl;
    MagickResetIterator(magick_wand);
    while (MagickNextImage(magick_wand) != MagickFalse)
    {
        MagickResizeImage(magick_wand, width / scaleValue, height / scaleValue, LanczosFilter);
    }
    status = MagickWriteImages(magick_wand, destPathStr, MagickTrue);
    return 0;
}

/*
 * Class:     com_wuxinbo_resourcemanage_jni_ImageMagick
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_ImageMagick_destroy(JNIEnv *, jclass)
{
}
