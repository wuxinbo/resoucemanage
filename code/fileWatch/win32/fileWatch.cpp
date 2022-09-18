#include "com_wuxinbo_resourcemanage_jni_FileWatch.h"
#include <Windows.h>
#include <iostream>


/**
 * wchar 转换为 char
 */
char *wchartoChar(LPCWCH wstr, size_t wstrLen)
{
    size_t length = wstrLen == 0 ? wcslen(wstr) : wstrLen;
    //计算多字节转换为单字节需要的字节数
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
LPCSTR watchDirChange(LPCWSTR dirName)
{
    HANDLE dirHandle = CreateFile(dirName, GENERIC_READ | GENERIC_WRITE,
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
    return wchartoChar(notifyInform->FileName, notifyInform->FileNameLength / sizeof(WCHAR));
}
//文件目录监控
JNIEXPORT jstring JNICALL Java_com_wuxinbo_resourcemanage_jni_FileWatch_watchDir(JNIEnv *env, jobject, jstring str)
{
    //从jstring 转换为wchar
    LPCSTR name = env->GetStringUTFChars(str, false);
    if (!name)
    {
        return 0;
    }

    LPCWSTR wdirName = multiByteToWideChar(name);
    LPCSTR fileName =watchDirChange(wdirName);
    env->ReleaseStringUTFChars(str, name);
    jstring jfileName = env->NewStringUTF(fileName);
    std::cout << "file isChange fileName is " << fileName << std::endl;
    delete []fileName;
    return jfileName;
}