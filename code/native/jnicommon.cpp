#include "jni.h"
#include <iostream>
#include "jni/comon.h"
JavaVM * jvm =nullptr;
std::mutex jniMutex;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    jvm = vm;
    std::cout << "jvm load " << jvm << std::endl;
    return JNI_VERSION_1_6;
}

std::mutex& getJniMutex(){
    return jniMutex;
}
 JavaVM * getjvm(){
    return jvm;
}
