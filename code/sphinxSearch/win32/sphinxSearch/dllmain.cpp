// dllmain.cpp : 定义 DLL 应用程序的入口点。
#include "pch.h"

SearchClient* client = nullptr;

dllExport SearchClient* initClient(const char* host,int port) {
    if (!client) {
        client = new SearchClient(host, port);
    }
    return client;
}
dllExport sphinx_result*  queryByKeyword(PCSTR keyword, PCSTR index) {
    return client->query(keyword,index);
}

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
                     )
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}

