#include "jni/comon.h"
#include "net/netcommon.h"
#include "server.h"
#include "client.h"
#include <iostream>
#include "TCPServerAndClient.h"
#include "logger.h"
// 启动tcp server
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_startServer(JNIEnv* env, jclass javaclass, jint jport)
{
    xbwuc::Logger::info("startServer", "start server","");
    int serverPort =jport==0?8080:jport;
    NET::TCPServer server;
    server.setTcpClass(javaclass);
    server.start(serverPort);
    
}

/**
 * java 数据发送接口
 */
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_sendUTFData(
    JNIEnv* env,
    jobject object, jstring jaddr, jstring jdata)
{
    jboolean iscopy = false;
    if (jstringNullCheck(env, jaddr) != 0) {
        std::cout << "addr is null" << std::endl;
        return;
    }
    if (jstringNullCheck(env, jdata) != 0) {
        std::cout << "jdata is null" << std::endl;
        return;
    }
    LPCSTR addr = env->GetStringUTFChars(jaddr, &iscopy);
    LPCSTR data = env->GetStringUTFChars(jdata, &iscopy);
    NET::TCPClient client;
    client.sendUTFData(addr, data);
    env->ReleaseStringUTFChars(jaddr, addr);
    env->ReleaseStringUTFChars(jdata, data);
}

