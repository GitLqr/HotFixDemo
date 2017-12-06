#include "com_lqr_jnitest_JniUtil.h"

JNIEXPORT jstring JNICALL Java_com_lqr_jnitest_JniUtil_hello
  (JNIEnv *env, jobject obj){
        return (*env)->NewStringUTF(env,"hello LQR");
  }