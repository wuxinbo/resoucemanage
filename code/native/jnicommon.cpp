#include "jni.h"
#include <iostream>
#include "jni/comon.h"
#include "logger.h"
JavaVM * jvm =nullptr;
std::mutex jniMutex;
jobject classLoader;
jclass tcpClass;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    jvm = vm;
    xbwuc::Logger::info("JNI_LOAD","jvm loaded","");
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload (JavaVM* vm, void* reserved){
    xbwuc::Logger::info("JNI_UNLOAD","jvm unload","");
    JNIEnv*  env ;
    vm->AttachCurrentThread(&env, nullptr);
    if (!env){
        return;
    }
    env->DeleteGlobalRef(classLoader);
    env->DeleteGlobalRef(tcpClass);
    vm->DetachCurrentThread();
}
std::mutex& getJniMutex(){
    return jniMutex;
}
JavaVM * getjvm(){
    return jvm;
}

jobject getClassLoader(){
    return classLoader;
}

jclass getTcpClass(){
    return tcpClass;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_wuxinbo_resourcemanage_jni_TCPServerClient_injectClassLoader(JNIEnv *env, jclass clazz,
                                                                      jobject class_loader) {
    classLoader =env->NewGlobalRef(class_loader);
    //查询 TCPClientclass
    tcpClass =env->FindClass("com/wuxinbo/resourcemanage/jni/TCPServerClient");
    if (!tcpClass){
        xbwuc::Logger::info("injectClassLoader","tcpClass is null ","");
        return;
    }
    tcpClass= static_cast<jclass>(env->NewGlobalRef(tcpClass));
}