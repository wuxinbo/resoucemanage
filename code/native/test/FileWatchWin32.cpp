

#include <iostream>
#include <Windows.h>
#include <memory>
#include <winnt.h>
#include "win32/common.h"
#include "logger.h"

struct FileNotify {
    unsigned long action;
    std::unique_ptr<char[]> filePath;
};
/**
 * @brief 使用同步的方式监听文件目录的变化
 *
 * @param dirName 目录路径
 */
void watchDirChange(const char * dirName,FileNotify &fileNotify)
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

    //  std::wcout << "filePath is "<< notifyInform->FileName <<" length is " 
    //  << notifyInform->FileNameLength <<"\n"<< std::endl;
    fileNotify.filePath =wchartoChar(notifyInform->FileName, notifyInform->FileNameLength/sizeof(WCHAR));
     fileNotify.action = notifyInform->Action;
 }
 
int main() {
    SetConsoleOutputCP(CP_UTF8); // 设置控制台输出为 UTF-8

    FileNotify fileNotify;
    
    watchDirChange("D:\\software",fileNotify);
    std::cout << "Hello, World! path " << fileNotify.filePath.get() << std::endl;

    return 0;
}

