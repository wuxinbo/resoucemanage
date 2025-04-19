

#ifdef _WIN32
    #ifdef XBWUC_NET_EXPORTS  // 定义这个宏表示正在编译 DLL
        #define XBWUC_NET_API __declspec(dllexport)
    #else
        #define XBWUC_NET_API __declspec(dllimport)
    #endif
#else
    #define XBWUC_NET_API  // Linux/macOS 不需要


#endif

#define NET xbwuc_net
#define NET_NAMESPACE_START namespace xbwuc_net {

#define NET_NAMESPACE_END };

#define xbwuc xbwuc
#define XBWUC_NAMESPACE_START namespace xbwuc {

#define XBWUC_NAMESPACE_END };


