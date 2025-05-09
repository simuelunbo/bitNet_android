package com.simuel.onebitllm

object BitnetNative {
    init {
        System.loadLibrary("bitnet")  // libbitnet.so
    }

    /** 모델 파일 경로를 받아 네이티브 컨텍스트를 초기화한다. */
    external fun initModel(modelPath: String, nThreads: Int = 4): Boolean

    /** 하나의 토큰(또는 문자열 버퍼)을 생성해 반환한다. */
    external fun generateNextToken(): String
}
