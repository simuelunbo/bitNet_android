package com.simuel.onebitllm.data.model

data class ModelLoadProgress(
    val bytesLoaded: Long = 0,
    val totalBytes: Long = 1, // 기본값 1로 0 나누기 방지
    val phase: LoadPhase = LoadPhase.INITIALIZATION
) {
    enum class LoadPhase {
        INITIALIZATION,
        PREPARING,
        LOADING,
        FINALIZING
    }
    
    val progressPercent: Float
        get() = (bytesLoaded.toFloat() / totalBytes.coerceAtLeast(1)).coerceIn(0f, 1f) * 100f
}
