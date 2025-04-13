#pragma once
#ifndef jni_common
#define jni_common 
#include "jni.h"
#include <iostream>
#include <mutex>
#define LPCSTR const char *


/**
 * jni 字符串检查
 */
inline int jstringNullCheck(JNIEnv *env, jstring jstr)
{
    if (jstr == NULL || env->GetStringLength(jstr) == 0)
    {
        std::cout << "src is null" << std::endl;
        return -1;
    }
    return 0;
}
extern JavaVM * jvm;
extern std::mutex jniMutex;
/**
 * 返回jvm 指针
 */
JavaVM * getjvm();
std::mutex& getJniMutex();

#endif