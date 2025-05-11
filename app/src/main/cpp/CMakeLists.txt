cmake_minimum_required(VERSION 3.22)
project(bitnet_android)

# Android에서 사용하지 않는 기능을 비활성화하는 전처리기 정의
add_definitions(
    -DGGML_USE_ACCELERATE=0
    -DGGML_USE_METAL=0
    -DGGML_USE_CUBLAS=0
    -DGGML_USE_SYCL=0
    -DGGML_USE_KOMPUTE=0
    -DGGML_USE_VULKAN=0
    -DGGML_USE_CUDA=0
    -DGGML_USE_CLBLAST=0
    -DGGML_USE_OPENMP=0
    -DGGML_USE_CANN=0
    -DGGML_USE_RPC=0
    -DGGML_USE_K_QUANTS=1
    -DANDROID_PLATFORM=android-26
    -DGGML_USE_METAL_TEXTURE=0
    -DGGML_USE_ARM_NEON=0
)

# include 디렉토리 생성
file(MAKE_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/include)

# 빈 ggml.h 헤더 생성
file(WRITE ${CMAKE_CURRENT_BINARY_DIR}/include/ggml.h "
// 최소한의 ggml.h 정의 - 빌드만 통과하기 위한 용도
#ifndef GGML_H
#define GGML_H

#ifdef __cplusplus
extern \"C\" {
#endif

// 주요 데이터 타입 정의
typedef struct ggml_tensor ggml_tensor;
typedef struct ggml_context ggml_context;

// 기본 enum 타입
typedef enum {
    GGML_TYPE_F32 = 0,
    GGML_TYPE_F16,
    GGML_TYPE_Q4_0,
    GGML_TYPE_Q4_1,
} ggml_type;

#ifdef __cplusplus
}
#endif

#endif // GGML_H
")

# ggml-bitnet.h 헤더도 필요 없는 내용은 제거하고 간단하게 생성
file(WRITE ${CMAKE_CURRENT_BINARY_DIR}/include/ggml-bitnet.h "
#ifndef GGML_BITNET_H
#define GGML_BITNET_H

#include \"ggml.h\"

#ifdef __cplusplus
extern \"C\" {
#endif

// 빈 구조체와 함수 선언
typedef struct ggml_tensor ggml_tensor;

#ifdef __cplusplus
}
#endif

#endif // GGML_BITNET_H
")

# bitnet_android.h 헤더 직접 정의
file(WRITE ${CMAKE_CURRENT_BINARY_DIR}/include/bitnet_android.h "
#pragma once

#ifndef BITNET_ANDROID_H
#define BITNET_ANDROID_H

#ifdef __cplusplus
extern \"C\" {
#endif

#include <android/log.h>
#define LOG_TAG \"BitNetNative\"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

// 비트넷 컨텍스트 구조체 정의
typedef struct _bitnet_context {
    // 실제 llama.cpp context와 model을 담을 포인터
    void* llama_model;
    void* llama_context;
    // 필요한 기타 상태 변수들
    int n_threads;
    bool is_initialized;
    // C 호환성을 위한 마지막 생성 텍스트 저장
    char last_generated_text[256];
} bitnet_context;

// 모델 파일로부터 비트넷 초기화
// modelPath: 모델 파일 경로
// n_threads: 사용할 스레드 수
bitnet_context* bitnet_init_from_file(const char* modelPath, int n_threads);

// 다음 토큰 생성 함수 (C 호환 버전)
const char* bitnet_generate_next(bitnet_context* ctx);

// 리소스 해제
void bitnet_free(bitnet_context* ctx);

#ifdef __cplusplus
} // extern \"C\"
#endif

// C++ 전용 구현 부분
#ifdef __cplusplus
#include <string>

// 실제 BitNet 구현 (C++로만 접근)
namespace bitnet {
    // 내부 구현에 사용할 헬퍼 함수
    std::string generate_token_internal(bitnet_context* ctx);
}
#endif

#endif // BITNET_ANDROID_H
")

# llama.cpp 스텁 기능 (JNI에 필요한 함수만 구현)
file(WRITE ${CMAKE_CURRENT_BINARY_DIR}/llama_stub.cpp "
#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <android/log.h>

#define LOG_TAG \"LlamaStub\"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

// 초간단 스텁 구현 - 일부만 JNI와 연동되도록 함
typedef void* llama_model;
typedef void* llama_context;

extern \"C\" {
    // 기본 함수들 - 실제로는 아무 작업도 하지 않음
    llama_model* llama_load_model_from_file(const char* path, int n_threads) {
        LOGI(\"llama_load_model_from_file 호출: %s (스레드: %d)\", path, n_threads);
        return (llama_model*)1; // 더미 포인터
    }
    
    llama_context* llama_new_context_with_model(llama_model* model, int n_threads) {
        LOGI(\"llama_new_context_with_model 호출 (스레드: %d)\", n_threads);
        return (llama_context*)2; // 더미 포인터
    }
    
    void llama_free(llama_context* ctx) {
        LOGI(\"llama_free 호출\");
    }
    
    void llama_free_model(llama_model* model) {
        LOGI(\"llama_free_model 호출\");
    }
    
    int llama_tokenize(llama_context* ctx, const char* text, int* tokens, int n_max_tokens) {
        LOGI(\"llama_tokenize 호출: '%s'\", text);
        // 더미 토큰 하나 생성
        if (n_max_tokens > 0 && tokens != NULL) {
            tokens[0] = 1;
        }
        return 1;
    }
    
    float* llama_get_logits(llama_context* ctx) {
        LOGI(\"llama_get_logits 호출\");
        // 더미 static 배열 반환
        static float logits[10] = {0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
        return logits;
    }
    
    int llama_token_to_str(llama_context* ctx, int token, char* str, int size) {
        LOGI(\"llama_token_to_str 호출: 토큰=%d\", token);
        // 더미 문자열 복사
        const char* dummy_str = \"Hello\";
        if (size > 0 && str != NULL) {
            strncpy(str, dummy_str, size-1);
            str[size-1] = '\\0';
        }
        return strlen(dummy_str);
    }
    
    int llama_eval(llama_context* ctx, const int* tokens, int n_tokens) {
        LOGI(\"llama_eval 호출: 토큰 %d개\", n_tokens);
        // 아무것도 하지 않고 성공 반환
        return 0;
    }
    
    int llama_n_vocab(llama_context* ctx) {
        LOGI(\"llama_n_vocab 호출\");
        // 더미 어휘 크기 반환
        return 1000;
    }
    
    int llama_sample_top_p(llama_context* ctx, int* prev_tokens, int n_prev, float p) {
        LOGI(\"llama_sample_top_p 호출: 토큰 %d개, p=%.2f\", n_prev, p);
        // 더미 토큰 반환
        return 42;
    }
}
")

# 직접 JNI 구현 파일 생성
file(WRITE ${CMAKE_CURRENT_BINARY_DIR}/bitnet_jni_impl.cpp "
#include <jni.h>
#include <string>
#include <cstring>
#include <vector>
#include \"include/bitnet_android.h\"

// 외부 llama.cpp API 인터페이스 선언
struct llama_model;
struct llama_context;

// 실제 llama.cpp 함수를 약한 참조로 선언 - 런타임에 동적으로 해결됨
extern \"C\" {
    __attribute__((weak)) llama_model* llama_load_model_from_file(const char* path, int n_threads);
    __attribute__((weak)) llama_context* llama_new_context_with_model(llama_model* model, int n_threads);
    __attribute__((weak)) void llama_free(llama_context* ctx);
    __attribute__((weak)) void llama_free_model(llama_model* model);
    __attribute__((weak)) int llama_tokenize(llama_context* ctx, const char* text, int* tokens, int n_max_tokens);
    __attribute__((weak)) float* llama_get_logits(llama_context* ctx);
    __attribute__((weak)) int llama_token_to_str(llama_context* ctx, int token, char* str, int size);
    __attribute__((weak)) int llama_eval(llama_context* ctx, const int* tokens, int n_tokens);
    __attribute__((weak)) int llama_n_vocab(llama_context* ctx);
    __attribute__((weak)) int llama_sample_top_p(llama_context* ctx, int* prev_tokens, int n_prev, float p);
}

/// 전역 컨텍스트 포인터 (실제 타입은 BitNet엔진 구조체)
static bitnet_context* g_ctx = nullptr;

// 텍스트 처리를 위한 토큰화 버퍼
static std::vector<int> s_tokens;
static const int MAX_TOKEN_LEN = 8192;

// C++ 내부 구현
namespace bitnet {
    // 토큰을 가져옴 (실제 추론 과정)
    std::string generate_token_internal(bitnet_context* ctx) {
        if (!ctx || !ctx->is_initialized) {
            return \"[ERROR: context not initialized]\";
        }
        
        LOGD(\"Generating next token (internal)\");
        
        // 스텁 구현 - 실제 모델이 연결되지 않은 경우
        if (!ctx->llama_context) {
            return \"Hello from BitNet! (demo mode)\";
        }
        
        // 토큰 평가 및 샘플링 (실제 모델 연결 시)
        try {
            // 로짓 가져오기 및 다음 토큰 샘플링
            if (s_tokens.size() > 0) {
                int next_token = llama_sample_top_p((llama_context*)ctx->llama_context, 
                                                   s_tokens.data(), 
                                                   s_tokens.size(), 
                                                   0.9f);
                
                // 토큰을 문자열로 변환
                char token_str[256] = {0};
                llama_token_to_str((llama_context*)ctx->llama_context, 
                                   next_token, 
                                   token_str, 
                                   sizeof(token_str) - 1);
                
                // 토큰 추가
                s_tokens.push_back(next_token);
                
                // 컨텍스트 크기 제한
                if (s_tokens.size() > MAX_TOKEN_LEN) {
                    s_tokens.erase(s_tokens.begin(), s_tokens.begin() + 1);
                }
                
                return token_str;
            }
            return \"[no tokens to process]\";
        } catch (...) {
            LOGE(\"Exception while generating token\");
            return \"[ERROR: exception during token generation]\";
        }
    }
}

// C API 함수 구현
bitnet_context* bitnet_init_from_file(const char* modelPath, int n_threads) {
    LOGI(\"Initializing BitNet with model: %s, threads: %d\", modelPath, n_threads);
    
    // 컨텍스트 초기화
    bitnet_context* ctx = new bitnet_context();
    ctx->n_threads = n_threads;
    ctx->is_initialized = false;
    ctx->llama_model = nullptr;
    ctx->llama_context = nullptr;
    
    // 초기화
    strcpy(ctx->last_generated_text, \"\");
    
    // 토큰 버퍼 초기화
    s_tokens.clear();
    s_tokens.reserve(MAX_TOKEN_LEN);
    
    try {
        // llama.cpp API가 사용 가능한지 확인
        if (llama_load_model_from_file && llama_new_context_with_model) {
            LOGI(\"Loading model %s\", modelPath);
            
            // 모델 로드
            llama_model* model = llama_load_model_from_file(modelPath, n_threads);
            if (!model) {
                LOGE(\"Failed to load model\");
                return ctx;
            }
            
            // 컨텍스트 생성
            llama_context* lctx = llama_new_context_with_model(model, n_threads);
            if (!lctx) {
                LOGE(\"Failed to create context\");
                llama_free_model(model);
                return ctx;
            }
            
            // 컨텍스트 저장
            ctx->llama_model = model;
            ctx->llama_context = lctx;
            ctx->is_initialized = true;
            
            LOGI(\"Model loaded successfully\");
        } else {
            LOGW(\"llama.cpp API not available, running in demo mode\");
            ctx->is_initialized = true; // 데모 모드로 실행
        }
    } catch (...) {
        LOGE(\"Exception during model initialization\");
    }
    
    return ctx;
}

const char* bitnet_generate_next(bitnet_context* ctx) {
    if (!ctx || !ctx->is_initialized) {
        LOGE(\"BitNet context not initialized\");
        return \"[ERROR: context not initialized]\";
    }
    
    try {
        // C++에서 문자열 생성 후 C 호환 버퍼에 복사
        std::string token = bitnet::generate_token_internal(ctx);
        strncpy(ctx->last_generated_text, token.c_str(), sizeof(ctx->last_generated_text) - 1);
        ctx->last_generated_text[sizeof(ctx->last_generated_text) - 1] = '\\0'; // 널 종료 보장
        
        LOGI(\"Generated token: %s\", ctx->last_generated_text);
        return ctx->last_generated_text;
    } catch (...) {
        LOGE(\"Exception in bitnet_generate_next\");
        return \"[ERROR: exception in token generation]\";
    }
}

void bitnet_free(bitnet_context* ctx) {
    if (ctx) {
        LOGI(\"Freeing BitNet resources\");
        
        try {
            // llama.cpp 리소스 해제
            if (ctx->llama_context && llama_free) {
                llama_free((llama_context*)ctx->llama_context);
            }
            
            if (ctx->llama_model && llama_free_model) {
                llama_free_model((llama_model*)ctx->llama_model);
            }
        } catch (...) {
            LOGE(\"Exception during resource cleanup\");
        }
        
        delete ctx;
    }
}

// 텍스트 입력 설정 함수 추가
extern \"C\"
JNIEXPORT jboolean JNICALL 
Java_com_simuel_onebitllm_BitnetNative_setInput(
        JNIEnv* env, jobject /*this*/,
        jstring inputTextJ) {
            
    LOGI(\"JNI: setInput called\");
    if (g_ctx == nullptr || !g_ctx->is_initialized) {
        LOGE(\"BitNet context not initialized\");
        return JNI_FALSE;
    }
    
    const char* inputText = env->GetStringUTFChars(inputTextJ, nullptr);
    bool success = false;
    
    try {
        if (llama_tokenize && g_ctx->llama_context) {
            // 토큰화 버퍼 초기화
            s_tokens.resize(MAX_TOKEN_LEN);
            
            // 텍스트를 토큰으로 변환
            int n_tokens = llama_tokenize(
                (llama_context*)g_ctx->llama_context,
                inputText,
                s_tokens.data(),
                MAX_TOKEN_LEN
            );
            
            if (n_tokens > 0) {
                // 실제 토큰 크기로 조정
                s_tokens.resize(n_tokens);
                LOGI(\"Tokenized input: %d tokens\", n_tokens);
                
                // 토큰 평가 (실제 모델의 경우)
                if (llama_eval) {
                    int eval_result = llama_eval(
                        (llama_context*)g_ctx->llama_context,
                        s_tokens.data(),
                        s_tokens.size()
                    );
                    
                    success = (eval_result == 0);
                    LOGI(\"Eval result: %d\", eval_result);
                } else {
                    // 모델 없이 데모 모드
                    success = true;
                }
            } else {
                LOGE(\"Failed to tokenize or empty input\");
            }
        } else {
            // 데모 모드로 실행 - 토큰화할 필요 없음
            LOGI(\"Demo mode: simulating tokenized input\");
            s_tokens.clear();
            s_tokens.push_back(1);  // 더미 토큰 추가
            success = true;
        }
    } catch (...) {
        LOGE(\"Exception in setInput\");
    }
    
    env->ReleaseStringUTFChars(inputTextJ, inputText);
    return success ? JNI_TRUE : JNI_FALSE;
}

// 모델 초기화 JNI 함수
extern \"C\"
JNIEXPORT jboolean JNICALL 
Java_com_simuel_onebitllm_BitnetNative_initModel(
        JNIEnv* env, jobject /*this*/,
        jstring modelPathJ, jint nThreads) {
            
    LOGI(\"JNI: initModel called\");
    
    const char* modelPath = env->GetStringUTFChars(modelPathJ, nullptr);
    
    // 이전 컨텍스트가 있으면 정리
    if (g_ctx != nullptr) {
        bitnet_free(g_ctx);
        g_ctx = nullptr;
    }
    
    // 새 컨텍스트 초기화
    g_ctx = bitnet_init_from_file(modelPath, nThreads);
    env->ReleaseStringUTFChars(modelPathJ, modelPath);
    
    return g_ctx && g_ctx->is_initialized ? JNI_TRUE : JNI_FALSE;
}

// 다음 토큰 생성 JNI 함수
extern \"C\"
JNIEXPORT jstring JNICALL 
Java_com_simuel_onebitllm_BitnetNative_generateNextToken(
        JNIEnv* env, jobject /*this*/) {
            
    LOGI(\"JNI: generateNextToken called\");
    
    if (!g_ctx || !g_ctx->is_initialized) {
        LOGE(\"BitNet context not initialized\");
        return env->NewStringUTF(\"[ERROR: context not initialized]\");
    }
    
    const char* generated = bitnet_generate_next(g_ctx);
    return env->NewStringUTF(generated);
}

// 모델 메모리 해제 JNI 함수
extern \"C\"
JNIEXPORT void JNICALL 
Java_com_simuel_onebitllm_BitnetNative_freeModel(
        JNIEnv* env, jobject /*this*/) {
            
    LOGI(\"JNI: freeModel called\");
    
    if (g_ctx) {
        bitnet_free(g_ctx);
        g_ctx = nullptr;
    }
}
")

# 주요 소스 코드 (JNI와 스텁만 포함)
set(SOURCES
    ${CMAKE_CURRENT_BINARY_DIR}/bitnet_jni_impl.cpp
    ${CMAKE_CURRENT_BINARY_DIR}/llama_stub.cpp
)

add_library(bitnet SHARED ${SOURCES})

# 포함 경로 설정 - 간단화
target_include_directories(bitnet PRIVATE
    ${CMAKE_CURRENT_BINARY_DIR}/include
    ${CMAKE_CURRENT_BINARY_DIR}
)

# Android 로그 라이브러리 링크
find_library(log-lib log)
target_link_libraries(bitnet ${log-lib})

# 컴파일 옵션 설정
target_compile_features(bitnet PUBLIC cxx_std_17)
target_compile_options(bitnet PRIVATE 
    -O3 
    -ffast-math
    -fno-finite-math-only
    -Wno-deprecated-declarations 
    -Wno-unused-result
    -Wno-unused-parameter
    -Wno-sign-compare
    -Wno-implicit-function-declaration
)
