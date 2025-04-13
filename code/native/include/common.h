

#ifdef _WIN32
    #ifdef XBWUC_NET_EXPORTS  // 定义这个宏表示正在编译 DLL
        #define XBWUC_NET_API __declspec(dllexport)
    #else
        #define XBWUC_NET_API __declspec(dllimport)
    #endif
#else
    #define XBWUC_NET_API  // Linux/macOS 不需要
#endif