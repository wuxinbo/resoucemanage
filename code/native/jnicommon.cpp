#include "jni.h"
#include <iostream>
#include "jni/comon.h"
JavaVM * jvm =nullptr;
std::mutex jniMutex;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    std::lock_guard<std::mutex> lock(jniMutex);
    jvm = vm;
    std::cout << "jvm load " << jvm <<" vm is" << vm << std::endl;
    return JNI_VERSION_1_8;
}

std::mutex& getJniMutex(){
    return jniMutex;
}
JNIEXPORT JavaVM * getjvm(){
    return jvm;
}
