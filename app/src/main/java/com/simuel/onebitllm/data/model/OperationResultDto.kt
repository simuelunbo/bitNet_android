package com.simuel.onebitllm.data.model

sealed class OperationResultDto {
    object Success : OperationResultDto()
    data class Error(val exception: Throwable) : OperationResultDto()
}