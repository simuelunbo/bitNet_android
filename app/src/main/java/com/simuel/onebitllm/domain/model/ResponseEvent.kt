package com.simuel.onebitllm.domain.model

sealed class ResponseEvent {
    object Started : ResponseEvent()

    data class TokenReceived(
        val token: String,
        val currentContent: String
    ) : ResponseEvent()

    data class Completed(val message: MessageData) : ResponseEvent()

    data class Failed(val error: Throwable) : ResponseEvent()
}