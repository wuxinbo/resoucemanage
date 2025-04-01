#pragma once
#include "jni.h"
#include <iostream>

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