#include <jni.h>
#include <string>
#include "bitnet_android.h"   // BitNet 엔진 헤더 (나중에 작성)

/// 전역 컨텍스트 포인터 (실제 타입은 BitNet엔진 구조체)
static bitnet_context* g_ctx = nullptr;

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_simuel_onebitllm_BitnetNative_initModel(
        JNIEnv* env, jobject /*this*/,
        jstring modelPathJ, jint nThreads) {

    const char* modelPath = env->GetStringUTFChars(modelPathJ, nullptr);

    // --- BitNet 엔진 호출 (예시) ---
    g_ctx = bitnet_init_from_file(modelPath, static_cast<int>(nThreads));
    // --------------------------------

    env->ReleaseStringUTFChars(modelPathJ, modelPath);
    return (g_ctx != nullptr);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_simuel_onebitllm_BitnetNative_generateNextToken(
        JNIEnv* env, jobject /*this*/) {

    if (g_ctx == nullptr) {
        return env->NewStringUTF("[not initialized]");
    }

    // --- BitNet 토큰 생성 예시 ---
    std::string token = bitnet_generate_next(g_ctx);
    // -----------------------------

    return env->NewStringUTF(token.c_str());
}
