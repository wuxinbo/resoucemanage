#include "jni/comon.h"
#include "server.h"
#include "client.h"
#include "TCPServerAndClient.h"
#include "logger.h"
// 启动tcp server
const char* getTag(){
    return "TCPServerClient";
}
extern "C"
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_startServer(JNIEnv* env, jclass javaclass, jint jport)
{
    LOG_INFO(getTag(),"start server");
    int serverPort =jport==0?8080:jport;
    NET::TCPServer server;
    server.start(serverPort);
    
}

/**
 * java 数据发送接口
 */
extern "C"
JNIEXPORT void JNICALL Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_sendUTFData(
    JNIEnv* env,
    jobject object, jstring jaddr, jstring jdata)
{
    jboolean iscopy = false;
    if (jstringNullCheck(env, jaddr) != 0) {
        LOG_INFO(getTag(), "addr is null");
        return;
    }
    if (jstringNullCheck(env, jdata) != 0) {
        LOG_INFO(getTag(), "jdata is null");
        return;
    }
    const char * addr = env->GetStringUTFChars(jaddr, &iscopy);
    const char * data = env->GetStringUTFChars(jdata, &iscopy);
    NET::TCPClient client;
    client.sendUTFData(addr, data);
    env->ReleaseStringUTFChars(jaddr, addr);
    env->ReleaseStringUTFChars(jdata, data);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_connect(JNIEnv *env, jclass clazz,
                                                            jstring jaddr) {
    // TODO: implement connect()
    jboolean iscopy = false;
    if (jstringNullCheck(env, jaddr) != 0) {
        LOG_INFO(getTag(), "addr is null");
        return;
    }
    const char * addr =env->GetStringUTFChars(jaddr,&iscopy);
    NET::TCPClient tcpClient;
    tcpClient.connect(addr);
    env->ReleaseStringUTFChars(jaddr,addr);
}