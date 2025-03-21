﻿#include <Windows.h>

#include "com_wuxinbo_resourcemanage_jni_FileWatch.h"
#include <iostream>

/**
 * wchar 转换为 char
 */
char *wchartoChar(LPCWCH wstr, size_t wstrLen)
{
    size_t length = wstrLen == 0 ? wcslen(wstr) : wstrLen;
    //计算宽字符串转换为多字节需要的字节数
    int size = WideCharToMultiByte(CP_UTF8, 0, wstr, length, nullptr, 0, 0, 0);
    char *data = new char[size + 1];
    WideCharToMultiByte(CP_UTF8, 0, wstr, length, data, size, 0, 0);
    data[size] = 0;
    return data;
}

/**
 * @brief 多字节转换为宽字符串
 *
 * @param str
 * @return LPCWCH
 */
LPCWSTR multiByteToWideChar(LPCSTR str)
{
    //首先获取字符大小
    int wsize = MultiByteToWideChar(CP_UTF8, 0, str, strlen(str), nullptr, 0);
    LPWSTR wdata = new WCHAR[wsize + 1];
    MultiByteToWideChar(CP_UTF8, 0, str, strlen(str), wdata, wsize);
    wdata[wsize] = 0;
    return wdata;
}


/**
 * @brief 使用同步的方式监听文件目录的变化
 *
 * @param dirName 目录路径
 */
JNIEXPORT void watchDirChange(const char * dirName,FileNotify &fileNotify)
{
    HANDLE dirHandle = CreateFileA(dirName, GENERIC_READ | GENERIC_WRITE,
                                  FILE_SHARE_WRITE | FILE_SHARE_READ,
                                  NULL,
                                  OPEN_EXISTING,              // dwCreationDisposition
                                  FILE_FLAG_BACKUP_SEMANTICS, // dwFlagsAndAttributes
                                  0L);
    byte notify[1024];
    FILE_NOTIFY_INFORMATION *notifyInform = (FILE_NOTIFY_INFORMATION *)&notify;
    DWORD lpBytesReturned = 100;
    //同步调用监听
    int result = ReadDirectoryChangesW(dirHandle,
                                       notifyInform,
                                       sizeof(notify),                                               // nBufferLength
                                       true,                                                         // bWatchSubtree
                                       FILE_NOTIFY_CHANGE_FILE_NAME | FILE_NOTIFY_CHANGE_LAST_WRITE, // dwNotifyFilter
                                       &lpBytesReturned,
                                       nullptr,
                                       0);
    //判断是否是文件名称修改了，如果是重命名则需要获取最新的文件名称,根据偏移量获取最新的文件信息。
    if (notifyInform->Action == FILE_ACTION_RENAMED_OLD_NAME) {
        notifyInform = (FILE_NOTIFY_INFORMATION*)(notify + notifyInform->NextEntryOffset);
    }
    fileNotify.filePath = wchartoChar(notifyInform->FileName, notifyInform->FileNameLength / sizeof(WCHAR));
    fileNotify.action = notifyInform->Action;
}

//文件目录监控
JNIEXPORT jobject JNICALL Java_com_wuxinbo_resourcemanage_jni_FileWatch_watchDir(JNIEnv *env, jobject, jstring str)
{
    jboolean iscopy =0;
    //从jstring 转换为wchar
    LPCSTR name = env->GetStringUTFChars(str, &iscopy);
    if (!name)
    {
        return 0;
    }

    // LPCWSTR wdirName = multiByteToWideChar(name);
    FileNotify fileNotify;
    watchDirChange(name,fileNotify);
    env->ReleaseStringUTFChars(str, name);
    jclass fileNotifyClass =env->FindClass("com/wuxinbo/resourcemanage/model/FileChangeNotify");
    if (!fileNotifyClass) {
        return  nullptr;
    }
    //构造对象
    jobject fileNotifyObj =env->AllocObject(fileNotifyClass);
    jstring jfileName = env->NewStringUTF(fileNotify.filePath);
    //设置文件路径
    jmethodID  setFilePathMethod= env->GetMethodID(fileNotifyClass, "setFilePath","(Ljava/lang/String;)V");
    env->CallVoidMethod(fileNotifyObj,setFilePathMethod,jfileName);

    jmethodID setAction =env->GetMethodID(fileNotifyClass,"setAction", "(I)V");
    env->CallVoidMethod(fileNotifyObj,setAction,fileNotify.action);
    std::cout << "file isChange fileName is " << fileNotify.filePath << std::endl;
    delete []fileNotify.filePath;
    return fileNotifyObj;
}
