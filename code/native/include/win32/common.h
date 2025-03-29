#ifdef WIN32
#include <Windows.h>
#include <memory>
#include <iostream> 
/**
 * wchar 转换为 char
 */


/**
 * @brief 多字节转换为宽字符串
 *#include <Windows.h>
#include <memory>
#include <cstring> // for strlen

/**
 * wchar 转换为 char
 */
std::unique_ptr<char[]> wchartoChar(LPCWCH wstr, size_t wstrLen)
{
    size_t length = wstrLen == 0 ? wcslen(wstr) : wstrLen;
    // 计算宽字符串转换为多字节需要的字节数
    int size = WideCharToMultiByte(CP_UTF8, 0, wstr, length, nullptr, 0, 0, 0);
    auto ptr =std::make_unique<char[]>(size + 1) ;
    WideCharToMultiByte(CP_UTF8, 0, wstr, length, ptr.get(), size, 0, 0);
    ptr.get()[size] = 0;
    return ptr;
}

/**
 * @brief 多字节转换为宽字符串
 *
 * @param str
 * @return std::shared_ptr<WCHAR>
 */
std::shared_ptr<WCHAR[]> multiByteToWideChar(LPCSTR str)
{
    // 首先获取字符大小
    int wsize = MultiByteToWideChar(CP_UTF8, 0, str, strlen(str), nullptr, 0);
    auto ptr =std::make_unique<WCHAR[]>(wsize + 1) ;
    MultiByteToWideChar(CP_UTF8, 0, str, strlen(str), ptr.get(), wsize);
    ptr.get()[wsize] = 0;
    return ptr;
}
#endif