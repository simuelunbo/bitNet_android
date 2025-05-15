//
// Created by sjb on 2025. 5. 7..
//

#ifndef BITNET_ANDROID_BITNET_JNI_H
#define BITNET_ANDROID_BITNET_JNI_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jboolean JNICALL Java_com_simuel_onebitllm_BitnetNative_initModel(JNIEnv *env, jobject thiz, jstring model_path, jint n_threads);
JNIEXPORT jboolean JNICALL Java_com_simuel_onebitllm_BitnetNative_setSystemPrompt(JNIEnv *env, jobject thiz, jstring prompt);
JNIEXPORT jboolean JNICALL Java_com_simuel_onebitllm_BitnetNative_setUserPrompt(JNIEnv *env, jobject thiz, jstring prompt);
JNIEXPORT jstring JNICALL Java_com_simuel_onebitllm_BitnetNative_generateNextToken(JNIEnv *env, jobject thiz);
JNIEXPORT void JNICALL Java_com_simuel_onebitllm_BitnetNative_freeModel(JNIEnv *env, jobject thiz);
JNIEXPORT jboolean JNICALL Java_com_simuel_onebitllm_BitnetNative_isModelLoaded(JNIEnv *env, jobject thiz);

#ifdef __cplusplus
}
#endif

#endif //BITNET_ANDROID_BITNET_JNI_H
