cmake_minimum_required(VERSION 3.22)
project(bitnet_android)

# 사용자 환경에서 llama.cpp 경로 지정
set(LLAMA_PATH "" CACHE PATH "Path to llama.cpp source")

# 기본 경로를 BitNet 서브모듈 내 포함된 llama.cpp 위치로 설정한다
if(NOT LLAMA_PATH)
    set(LLAMA_PATH "${CMAKE_CURRENT_SOURCE_DIR}/bitnet/3rdparty/llama.cpp" CACHE PATH "Path to llama.cpp source" FORCE)
endif()

if(NOT EXISTS "${LLAMA_PATH}/llama.h")
    message(FATAL_ERROR "LLAMA_PATH does not contain llama.cpp sources")
endif()

add_subdirectory(${LLAMA_PATH} external_llama)

# BitNet JNI 구현
set(SOURCES
    bitnet_jni.cpp
)

add_library(bitnet SHARED ${SOURCES})

# 헤더 포함 경로
target_include_directories(bitnet PRIVATE
    ${LLAMA_PATH}
    ${LLAMA_PATH}/common
    ${CMAKE_CURRENT_SOURCE_DIR}/include
)

# Android 로그 라이브러리
find_library(log-lib log)

# 링크할 llama.cpp 라이브러리 이름은 'llama'
target_link_libraries(bitnet
    PRIVATE
    llama
    ${log-lib}
)

# 컴파일 옵션
target_compile_features(bitnet PUBLIC cxx_std_17)
target_compile_options(bitnet PRIVATE
    -O3
    -ffast-math
    -fno-finite-math-only
)
