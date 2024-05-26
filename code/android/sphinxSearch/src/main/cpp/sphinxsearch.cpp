#include <jni.h>
#include <string>
#include "searchClient.h"
#include <iostream>
#include "unistd.h"
#include "android/log.h"
extern "C" JNIEXPORT jstring JNICALL
Java_com_wu_sphinxsearch_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    char hostname[100];
    gethostname(hostname,100);

    return env->NewStringUTF(hostname);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_wu_sphinxsearch_NativeLib_query(JNIEnv *env, jobject thiz, jstring keyword,
                                         jstring index) {
    // TODO: implement query()
    SearchClient *searchClient =new SearchClient("192.168.2.3",9312);
    sphinx_result  * res =searchClient->query(env->GetStringUTFChars(keyword,0),env->GetStringUTFChars(index,0));
    jstring msg ;
    __android_log_write(ANDROID_LOG_DEBUG,"sphinxSearch","res is null");
    if (res){
        msg = env->NewStringUTF("success");
    }else{
        std::cout<< "query fail" <<std::endl;
#ifdef ANDROID
        msg=env->NewStringUTF("android fail");
#else
        msg=env->NewStringUTF("fail");
#endif
    }
    return msg;
}
