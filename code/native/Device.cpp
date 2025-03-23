
#include <winsock2.h>
#include <iostream>
#include <string>
#pragma comment(lib, "Bthprops.lib")
#pragma comment(lib, "Ws2_32.lib")
#include "common.h"
#include <Bthsdpdef.h>
#include <ws2bth.h>
#include <bluetoothapis.h>
#include <sstream>
#include <iomanip>
#include <initguid.h>
#include <thread>
const int MAX_BUFFER_SIZE = 2048;
GUID uuidSerialService = { 0x00001801, 0x0000, 0x1000, {0x80, 0x00, 0x00, 0x80, 0x5F, 0x9B, 0x34, 0xFB} };
// B020-CF1E77BC9123
GUID xbwuc ={ 0x371B664F, 0x03A5, 0x4193, {0xB0, 0x20, 0xCF, 0x1E, 0x77, 0xBC, 0x91, 0x23} };
void printError()
{
    int errorCode = WSAGetLastError();
    char errorMsg[256];
    FormatMessageA(
        FORMAT_MESSAGE_FROM_SYSTEM,
        nullptr,
        errorCode,
        0,
        errorMsg,
        sizeof(errorMsg),
        nullptr);
    printf("错误码: %d, 错误信息: %s\n", errorCode, errorMsg);    
}
void receviceData(SOCKET sock) {
    char receiveData[1024] = {0};
    while (true)
    {
        int size =recv(sock, receiveData, 100, 0);
        if (size > 0) {
            std::cout << "recevie data " << receiveData << std::endl;
        }
        else if (size == 0) {
            std::cout << "没有数据可以接受" << receiveData << std::endl;
            break;
        }


    }

}
void connectOppo(SOCKADDR_BTH* addr){

     addr->serviceClassId= uuidSerialService;
    // 创建套接字
    SOCKET s = socket(AF_BTH, SOCK_STREAM, BTHPROTO_RFCOMM);
    if (s == INVALID_SOCKET)
    {
        printError();
        std::cerr << "创建套接字失败" << std::endl;
        return;
    }
     std::cout << "尝试连接的蓝牙地址: " << std::hex << addr->btAddr << std::endl;
    // 连接到设备
    int result = connect(s, (SOCKADDR *)addr, sizeof(SOCKADDR_BTH));
    if (result == SOCKET_ERROR)
    {
        printError();
        std::cerr << "连接到设备失败" << std::endl;
        closesocket(s);
        return;
    }
    std::thread t(receviceData,s);
    for (size_t i = 0; i < 100; i++)
    {
        std::string data = "你好啊!";
        data+=std::to_string(i);
        std::cout << "msg is send data is "<<data.c_str() << std::endl;
        send(s, data.c_str(), strlen(data.c_str()), 0);
    }
    

    if (result == SOCKET_ERROR)
    {
        printError();
        std::cerr << "发送数据失败" << std::endl;
        closesocket(s);
        return;
    }
    t.join();
    std::cout << "msg is send done" << std::endl;
    // 关闭套接字
    closesocket(s);
}

int main()
{
    SetConsoleOutputCP(CP_UTF8);

    // 初始化蓝牙设备搜索参数
    BLUETOOTH_DEVICE_SEARCH_PARAMS searchParams = {sizeof(BLUETOOTH_DEVICE_SEARCH_PARAMS)};
    // 设置搜索参数，返回已认证的设备
    searchParams.fReturnAuthenticated = TRUE;
    // 设置搜索参数，返回已记住的设备
    searchParams.fReturnRemembered = TRUE;
    // 设置搜索参数，返回未知的设备
    searchParams.fReturnUnknown = TRUE;
    // 设置搜索参数，返回已连接的设备
    searchParams.fReturnConnected = TRUE;
    // 设置搜索参数，超时时间乘以4，以增加搜索耐心
    searchParams.cTimeoutMultiplier = 4;

    BLUETOOTH_DEVICE_INFO deviceInfo = {sizeof(BLUETOOTH_DEVICE_INFO)};
    HBLUETOOTH_DEVICE_FIND hFind = BluetoothFindFirstDevice(&searchParams, &deviceInfo);
    if (hFind)
    {
        do
        {
            std::stringstream ss;
            ss << " 地址：" << std::uppercase << std::hex << (int)deviceInfo.Address.rgBytes[5]
               << ":" << (int)deviceInfo.Address.rgBytes[4]
               << ":" << (int)deviceInfo.Address.rgBytes[3]
               << ":" << (int)deviceInfo.Address.rgBytes[2]
               << ":" << (int)deviceInfo.Address.rgBytes[1]
               << ":" << (int)deviceInfo.Address.rgBytes[0];
            std::string infostr = "设备名称: ";
            infostr += wchartoChar(deviceInfo.szName, 0).get();
            infostr += " 是否已连接: ";
            infostr += deviceInfo.fConnected ? "已连接" : "未连接。";
            infostr += ss.str();
            std::cout << infostr << std::endl;
        } while (BluetoothFindNextDevice(hFind, &deviceInfo));
        BluetoothFindDeviceClose(hFind);
    }

    DWORD flags =LUP_RETURN_NAME | LUP_RETURN_ADDR| LUP_FLUSHCACHE;
    WSAQUERYSET querySet = {0};
    querySet.lpServiceClassId =(LPGUID) &xbwuc;
    querySet.dwSize = sizeof(WSAQUERYSET);
    querySet.dwNameSpace = NS_BTH;
    WSAData wsaData;
    int code;
    code = WSAStartup(MAKEWORD(2, 2), &wsaData);
    if (code != 0)
    {
        printError();
        std::cerr << "WSAStartup 失败: " << code << std::endl;
        return code;
    }
    HANDLE hLookup;
    SOCKADDR_BTH targetAddr = {0};
    targetAddr.addressFamily = AF_BTH;
    targetAddr.btAddr = deviceInfo.Address.ullLong; // 蓝牙地址转换
    targetAddr.port = BT_PORT_ANY; // 自动分配端口
    char address[100];
    DWORD lpdwSize=100;
    code =WSAAddressToString(
        (LPSOCKADDR)&targetAddr,
        sizeof(SOCKADDR_BTH),
        nullptr,
        address,
        &lpdwSize
    );
    if(code!=0){
        printError();
        std::cerr << "地址转换失败: " << code << std::endl;
        return code;
    }

    querySet.lpszContext =address;
    querySet.lpBlob = nullptr;
    DWORD result = WSALookupServiceBegin(&querySet, flags, &hLookup);
    if (result != ERROR_SUCCESS)
    {
        printError();
        std::cerr << "服务查询失败: " << result << std::endl;
        return result;
    }

    // 遍历服务
    CHAR buffer[MAX_BUFFER_SIZE] = {0};
    LPWSAQUERYSET pResults = (LPWSAQUERYSET)buffer;
    DWORD bufferSize =MAX_BUFFER_SIZE;
    SOCKADDR_BTH *pAddr=nullptr ;
    do
    {
        pResults->dwSize = sizeof(WSAQUERYSET);
        result = WSALookupServiceNext(hLookup, flags, &bufferSize, pResults);
        if (result == ERROR_SUCCESS)
        {
            pAddr = (SOCKADDR_BTH *)pResults->lpcsaBuffer->RemoteAddr.lpSockaddr;
			std::cout << "找到的设备名称：" << pResults->lpszServiceInstanceName << std::endl;
        }
         else{
             printError();
         }
    } while (result == ERROR_SUCCESS);
    WSALookupServiceEnd(hLookup);
    if(pAddr){
        connectOppo(pAddr);
    }

    return 0;
}