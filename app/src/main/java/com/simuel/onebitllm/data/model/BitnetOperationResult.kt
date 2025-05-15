package com.simuel.onebitllm.data.model

sealed class BitnetOperationResult {
    object Success : BitnetOperationResult()
    data class Error(val exception: Throwable) : BitnetOperationResult()
}