package com.simuel.onebitllm.domain.model

sealed class OperationResult<out T> {
    data class Success<T>(val data: T) : OperationResult<T>()
    data class Failure(
        val errorCode: ErrorCode,
        val message: String,
        val exception: Throwable? = null
    ) : OperationResult<Nothing>()
}