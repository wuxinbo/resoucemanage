#include <jni.h>
#include <string>
#include "searchClient.h"
#include <iostream>
extern "C" JNIEXPORT jstring JNICALL
Java_com_wu_sphinxsearch_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_wu_sphinxsearch_NativeLib_query(JNIEnv *env, jobject thiz, jstring keyword,
                                         jstring index) {
    // TODO: implement query()
    SearchClient *searchClient =new SearchClient("192.168.2.3",9312);
    sphinx_result  * res =searchClient->query(env->GetStringUTFChars(keyword,0),env->GetStringUTFChars(index,0));
    if (res){
        std::cout<< "query success" <<std::endl;
        return env->NewStringUTF("success");
    }else{
        std::cout<< "query fail" <<std::endl;
        return env->NewStringUTF("fail");
    }
}