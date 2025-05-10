#include "TCPServerAndClient.h"
#include "client.h"
#include "jni.h"
#include "jni/comon.h"
#include "logger.h"
#include "server.h"
#include <iostream>
#include <mutex>
#include <stdexcept>

void invokeJavaRecive(std::string &data) {
  std::lock_guard<std::mutex> lock(getJniMutex());
  ;
  JNIEnv *jnienv = nullptr;
  JavaVM *jvm = getjvm();
  jint version = JNI_VERSION_1_6;
#ifdef ANDROID
  jint res = jvm->AttachCurrentThread(&jnienv, nullptr);
  version = JNI_VERSION_1_6;
#else
  jint res = jvm->AttachCurrentThread((void **)&jnienv, nullptr);
#endif
#ifdef WIN32
  version = JNI_VERSION_1_8;
#endif
  jvm->GetEnv((void **)&jnienv, version);
  if (!jnienv) {
    std::cout << "get env fail" << std::endl;
    return;
  }
  jclass tcpClass = getTcpClass();
  if (!tcpClass) {
    LOG_INFO("tcpClientClass is null ");
    return;
  }
  jmethodID method = jnienv->GetStaticMethodID(tcpClass, "receiveData",
                                               "(Ljava/lang/String;)V");
  if (method == nullptr) {
    // 打印异常信息
    jnienv->ExceptionDescribe();
    jnienv->ExceptionClear();
    return; // 或处理错误
  }
  jstring jstr = jnienv->NewStringUTF(data.c_str());
  jnienv->CallStaticVoidMethod(tcpClass, method, jstr);
  jvm->DetachCurrentThread();
}
// 转发客户端信息到jvm
void dispatchConnectToJvm(const char *address) {
  JNIEnv *jnienv = nullptr;
  JavaVM *jvm = getjvm();
  jint version = JNI_VERSION_1_6;
#ifdef ANDROID
  jint res = jvm->AttachCurrentThread(&jnienv, nullptr);
  version = JNI_VERSION_1_6;
#else
  jint res = jvm->AttachCurrentThread((void **)&jnienv, nullptr);
#endif
#ifdef WIN32
  version = JNI_VERSION_1_8;
#endif
  jvm->GetEnv((void **)&jnienv, version);
  if (!jnienv) {
    std::cout << "get env fail" << std::endl;
    return;
  }
  jclass tcpClass = getTcpClass();
  if (!tcpClass) {
    LOG_INFO("tcpClientClass is null ");
    return;
  }
  jmethodID method = jnienv->GetStaticMethodID(tcpClass, "receiveClient",
                                               "(Ljava/lang/String;)V");
  if (method == nullptr) {
    std::cout << "method is null" << std::endl;
    // 打印异常信息
    jnienv->ExceptionDescribe();
    jnienv->ExceptionClear();
    return; // 或处理错误
  }
  jstring jstr = jnienv->NewStringUTF(address);
  jnienv->CallStaticVoidMethod(tcpClass, method, jstr);
}
// 启动tcp server
extern "C" JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_startServer(
    JNIEnv *env, jclass javaclass, jint jport) {

  LOG_INFO("start server");
  int serverPort = jport == 0 ? 8080 : jport;
  NET::TCPServer server;
  // 修复错误 E0079: 应输入类型说明符
  // 原因是 lambda 表达式的返回类型未指定，导致编译器无法推断类型。
  // 添加明确的返回类型 void。
  server.registerDataReceiveFunc([](std::string data) -> void {
    // 添加处理逻辑
    LOG_INFO_DATA("Data received: %s", data);
    invokeJavaRecive(data);
  });
  server.registerClientConnectFunc([](std::string address) -> void {
    LOG_INFO_DATA("Client connected: %s", address);
    dispatchConnectToJvm(address.c_str());
  });
  server.start(serverPort);
}

/**
 * java 数据发送接口
 */
extern "C" JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_sendUTFData(JNIEnv *env,
                                                                jobject object,
                                                                jstring jaddr,
                                                                jstring jdata) {
  jboolean iscopy = false;
  if (jstringNullCheck(env, jaddr) != 0) {
    LOG_INFO("addr is null");
    return;
  }
  if (jstringNullCheck(env, jdata) != 0) {
    LOG_INFO("jdata is null");
    return;
  }
  const char *addr = env->GetStringUTFChars(jaddr, &iscopy);
  const char *data = env->GetStringUTFChars(jdata, &iscopy);
  NET::TCPClient client;
  client.sendUTFData(addr, data);
  env->ReleaseStringUTFChars(jaddr, addr);
  env->ReleaseStringUTFChars(jdata, data);
}

JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_sendUTFDataToClient(
    JNIEnv *env, jobject object, jstring jaddr, jstring jdata) {
  jboolean iscopy = false;
  if (jstringNullCheck(env, jaddr) != 0) {
    LOG_INFO("addr is null");
    return;
  }
  if (jstringNullCheck(env, jdata) != 0) {
    LOG_INFO("jdata is null");
    return;
  }
  const char *addr = env->GetStringUTFChars(jaddr, &iscopy);
  const char *data = env->GetStringUTFChars(jdata, &iscopy);
  NET::TCPServer tcpServer;
  tcpServer.sendUTFData(addr, data);
  env->ReleaseStringUTFChars(jaddr, addr);
  env->ReleaseStringUTFChars(jdata, data);
}

extern "C" JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_connect(JNIEnv *env,
                                                            jclass clazz,
                                                            jstring jaddr) {
  jboolean iscopy = false;
  if (jstringNullCheck(env, jaddr) != 0) {
    LOG_INFO("addr is null");
    return;
  }
  const char *addr = env->GetStringUTFChars(jaddr, &iscopy);
  NET::TCPClient tcpClient;
  tcpClient.connect(addr);
  env->ReleaseStringUTFChars(jaddr, addr);
}
