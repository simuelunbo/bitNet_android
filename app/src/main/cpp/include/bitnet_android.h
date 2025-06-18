#pragma once

#ifndef BITNET_ANDROID_H
#define BITNET_ANDROID_H

#ifdef __cplusplus
extern "C" {
#endif

#include <android/log.h>
#define LOG_TAG "BitNetNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

// 비트넷 컨텍스트 구조체 정의
typedef struct _bitnet_context {
    void* llama_model;
    void* llama_context;
    int n_threads;
    bool is_initialized;
    char last_generated_text[256];
} bitnet_context;

bitnet_context* bitnet_init_from_file(const char* modelPath, int n_threads);
const char* bitnet_generate_next(bitnet_context* ctx);
void bitnet_free(bitnet_context* ctx);

#ifdef __cplusplus
} // extern "C"
#endif

#ifdef __cplusplus
#include <string>
namespace bitnet {
    std::string generate_token_internal(bitnet_context* ctx);
}
#endif

#endif // BITNET_ANDROID_H
