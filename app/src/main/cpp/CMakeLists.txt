cmake_minimum_required(VERSION 3.22)
project(bitnet_android)

## A. BitNet 커널 소스
#file(GLOB BITNET_SRC
#        "bitnet/src/*.cpp")

# A. JNI 브리지
set(JNI_BRIDGE bitnet_jni.cpp)


# B. llama.cpp + ggml 소스
file(GLOB LLAMA_SRC
        "bitnet/src/*.cpp"
        "bitnet/3rdparty/llama.cpp/*.cpp"
        "bitnet/3rdparty/llama.cpp/ggml/*.c")

#add_library(bitnet SHARED ${BITNET_SRC} ${LLAMA_SRC})
add_library(bitnet SHARED ${JNI_BRIDGE} ${BITNET_SRC})

# C. 포함 경로
target_include_directories(bitnet PRIVATE
        bitnet/include
        bitnet/3rdparty/llama.cpp
        bitnet/3rdparty/llama.cpp/ggml)

# D. 안드로이드 로그 라이브러리 링크
find_library(log-lib log)
target_link_libraries(bitnet ${log-lib})

target_compile_features(bitnet PUBLIC cxx_std_17)
target_compile_options(bitnet PRIVATE -O3 -ffast-math)
target_compile_definitions(bitnet PRIVATE GGML_USE_ACCELERATE=0)