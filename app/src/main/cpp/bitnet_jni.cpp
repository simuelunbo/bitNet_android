#include <jni.h>
#include <string>
#include <cstring>
#include <vector>
#include "bitnet/include/bitnet_android.h"

// 로그 매크로 정의
#include <android/log.h>
#define LOG_TAG "BitNetNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

// 외부 llama.cpp API 인터페이스 선언
struct llama_model;
struct llama_context;

// 실제 llama.cpp 함수를 약한 참조로 선언 - 런타임에 동적으로 해결됨
extern "C" {
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

// 전역 변수로 시스템 프롬프트와 사용자 프롬프트 토큰 저장
static std::vector<llama_token> g_system_tokens;
static std::vector<llama_token> g_user_tokens;
static int g_n_past = 0;

#ifdef __cplusplus
// C++ 내부 구현
namespace bitnet {
    // 토큰을 가져옴 (실제 추론 과정)
    std::string generate_token_internal(bitnet_context* ctx) {
        if (!ctx || !ctx->is_initialized) {
            return "[ERROR: context not initialized]";
        }
        
        LOGD("Generating next token (internal)");
        
        // 스텁 구현 - 실제 모델이 연결되지 않은 경우
        if (!ctx->llama_context) {
            return "Hello from BitNet! (demo mode)";
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
            return "[no tokens to process]";
        } catch (...) {
            LOGE("Exception while generating token");
            return "[ERROR: exception during token generation]";
        }
    }
}
#endif

// C API 함수 구현
bitnet_context* bitnet_init_from_file(const char* modelPath, int n_threads) {
    LOGI("Initializing BitNet with model: %s, threads: %d", modelPath, n_threads);
    
    // 컨텍스트 초기화
    bitnet_context* ctx = new bitnet_context();
    ctx->n_threads = n_threads;
    ctx->is_initialized = false;
    ctx->llama_model = nullptr;
    ctx->llama_context = nullptr;
    
    // 초기화
    strcpy(ctx->last_generated_text, "");
    
    // 토큰 버퍼 초기화
    s_tokens.clear();
    s_tokens.reserve(MAX_TOKEN_LEN);
    
    try {
        // llama.cpp API가 사용 가능한지 확인
        if (llama_load_model_from_file && llama_new_context_with_model) {
            LOGI("Loading model %s", modelPath);
            
            // 모델 로드
            llama_model* model = llama_load_model_from_file(modelPath, n_threads);
            if (!model) {
                LOGE("Failed to load model");
                return ctx;
            }
            
            // 컨텍스트 생성
            llama_context* lctx = llama_new_context_with_model(model, n_threads);
            if (!lctx) {
                LOGE("Failed to create context");
                llama_free_model(model);
                return ctx;
            }
            
            // 컨텍스트 저장
            ctx->llama_model = model;
            ctx->llama_context = lctx;
            ctx->is_initialized = true;
            
            LOGI("Model loaded successfully");
        } else {
            LOGW("llama.cpp API not available, running in demo mode");
            ctx->is_initialized = true; // 데모 모드로 실행
        }
    } catch (...) {
        LOGE("Exception during model initialization");
    }
    
    return ctx;
}

const char* bitnet_generate_next(bitnet_context* ctx) {
    if (!ctx || !ctx->is_initialized) {
        LOGE("BitNet context not initialized");
        return "[ERROR: context not initialized]";
    }
    
    try {
        // C++에서 문자열 생성 후 C 호환 버퍼에 복사
        std::string token = bitnet::generate_token_internal(ctx);
        strncpy(ctx->last_generated_text, token.c_str(), sizeof(ctx->last_generated_text) - 1);
        ctx->last_generated_text[sizeof(ctx->last_generated_text) - 1] = '\0'; // 널 종료 보장
        
        LOGI("Generated token: %s", ctx->last_generated_text);
        return ctx->last_generated_text;
    } catch (...) {
        LOGE("Exception in bitnet_generate_next");
        return "[ERROR: exception in token generation]";
    }
}

void bitnet_free(bitnet_context* ctx) {
    if (ctx) {
        LOGI("Freeing BitNet resources");
        
        try {
            // llama.cpp 리소스 해제
            if (ctx->llama_context && llama_free) {
                llama_free((llama_context*)ctx->llama_context);
            }
            
            if (ctx->llama_model && llama_free_model) {
                llama_free_model((llama_model*)ctx->llama_model);
            }
        } catch (...) {
            LOGE("Exception during resource cleanup");
        }
        
        delete ctx;
    }
}

// 텍스트 입력 설정 함수 추가
extern "C"
JNIEXPORT jboolean JNICALL 
Java_com_simuel_onebitllm_BitnetNative_setInput(
        JNIEnv* env, jobject /*this*/,
        jstring inputTextJ) {
            
    LOGI("JNI: setInput called");
    if (g_ctx == nullptr || !g_ctx->is_initialized) {
        LOGE("BitNet context not initialized");
        return JNI_FALSE;
    }
    
    const char* inputText = env->GetStringUTFChars(inputTextJ, nullptr);
    bool success = false;
    
    try {
        LOGI("Setting input: %s", inputText);
        
        // 토큰화 버퍼 초기화
        s_tokens.clear();
        
        // 실제 모델이 로드된 경우
        if (g_ctx->llama_context && llama_tokenize) {
            // 입력 텍스트 토큰화
            s_tokens.resize(MAX_TOKEN_LEN);
            int n_tokens = llama_tokenize((llama_context*)g_ctx->llama_context, 
                                         inputText, 
                                         s_tokens.data(), 
                                         s_tokens.size());
            
            if (n_tokens < 0) {
                LOGE("Tokenization failed");
            } else {
                s_tokens.resize(n_tokens);
                LOGI("Input tokenized: %d tokens", n_tokens);
                
                // 토큰 평가 (모델 실행)
                if (llama_eval) {
                    int ret = llama_eval((llama_context*)g_ctx->llama_context, 
                                        s_tokens.data(), 
                                        s_tokens.size());
                    success = (ret == 0);
                }
            }
        } else {
            // 데모 모드
            success = true;
        }
    } catch (...) {
        LOGE("Exception in setInput");
    }
    
    env->ReleaseStringUTFChars(inputTextJ, inputText);
    return success ? JNI_TRUE : JNI_FALSE;
}

// JNI 함수 구현
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_simuel_onebitllm_BitnetNative_initModel(
        JNIEnv* env, jobject /*this*/,
        jstring modelPathJ, jint nThreads) {

    LOGI("JNI: initModel called");
    const char* modelPath = env->GetStringUTFChars(modelPathJ, nullptr);

    // BitNet 엔진 호출
    g_ctx = bitnet_init_from_file(modelPath, static_cast<int>(nThreads));

    env->ReleaseStringUTFChars(modelPathJ, modelPath);
    return (g_ctx != nullptr && g_ctx->is_initialized) ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_simuel_onebitllm_BitnetNative_generateNextToken(
        JNIEnv* env, jobject /*this*/) {

    LOGI("JNI: generateNextToken called");
    if (g_ctx == nullptr) {
        LOGE("BitNet context is null");
        return env->NewStringUTF("[not initialized]");
    }

    // 다음 토큰 생성
    llama_token new_token_id = llama_sampling_sample(g_ctx, nullptr, nullptr);
    llama_sampling_accept(g_ctx, new_token_id, true);

    // 토큰을 문자열로 변환
    const char *token_str = llama_token_to_piece(g_ctx, new_token_id).c_str();
    if (!token_str) {
        return nullptr;
    }

    // 토큰 평가
    llama_batch batch = llama_batch_init(1, 0, 1);
    llama_batch_add(batch, new_token_id, g_n_past, { 0 }, false);
    if (llama_decode(g_ctx, batch) != 0) {
        return nullptr;
    }
    g_n_past++;

    return env->NewStringUTF(token_str);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simuel_onebitllm_BitnetNative_freeModel(
        JNIEnv* env, jobject /*this*/) {
    
    LOGI("JNI: freeModel called");
    if (g_ctx != nullptr) {
        bitnet_free(g_ctx);
        g_ctx = nullptr;
    }
    if (g_model != nullptr) {
        llama_free_model(g_model);
        g_model = nullptr;
    }
    // 프롬프트 토큰 초기화
    g_system_tokens.clear();
    g_user_tokens.clear();
    g_n_past = 0;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_simuel_onebitllm_BitnetNative_setSystemPrompt(JNIEnv *env, jobject thiz, jstring prompt) {
    if (!g_ctx || !g_model) {
        return JNI_FALSE;
    }

    const char *system_prompt = env->GetStringUTFChars(prompt, nullptr);
    if (!system_prompt) {
        return JNI_FALSE;
    }

    // 시스템 프롬프트 토큰화
    g_system_tokens = llama_tokenize(g_model, system_prompt, true, true);
    env->ReleaseStringUTFChars(prompt, system_prompt);

    if (g_system_tokens.empty()) {
        return JNI_FALSE;
    }

    // 시스템 프롬프트 평가
    g_n_past = 0;
    for (size_t i = 0; i < g_system_tokens.size(); i++) {
        llama_batch batch = llama_batch_init(1, 0, 1);
        llama_batch_add(batch, g_system_tokens[i], i, { 0 }, false);
        if (llama_decode(g_ctx, batch) != 0) {
            return JNI_FALSE;
        }
        g_n_past++;
    }

    return JNI_TRUE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_simuel_onebitllm_BitnetNative_setUserPrompt(JNIEnv *env, jobject thiz, jstring prompt) {
    if (!g_ctx || !g_model) {
        return JNI_FALSE;
    }

    const char *user_prompt = env->GetStringUTFChars(prompt, nullptr);
    if (!user_prompt) {
        return JNI_FALSE;
    }

    // 사용자 프롬프트 토큰화
    g_user_tokens = llama_tokenize(g_model, user_prompt, false, true);
    env->ReleaseStringUTFChars(prompt, user_prompt);

    if (g_user_tokens.empty()) {
        return JNI_FALSE;
    }

    // 사용자 프롬프트 평가
    for (size_t i = 0; i < g_user_tokens.size(); i++) {
        llama_batch batch = llama_batch_init(1, 0, 1);
        llama_batch_add(batch, g_user_tokens[i], g_n_past + i, { 0 }, false);
        if (llama_decode(g_ctx, batch) != 0) {
            return JNI_FALSE;
        }
    }
    g_n_past += g_user_tokens.size();

    return JNI_TRUE;
}
