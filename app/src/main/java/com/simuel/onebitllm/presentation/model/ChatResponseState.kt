package com.simuel.onebitllm.presentation.model

import com.simuel.onebitllm.domain.model.ChatMessage

sealed class ChatResponseState {
    object Started : ChatResponseState()

    data class TokenReceived(
        val token: String,
        val currentContent: String
    ) : ChatResponseState()

    data class Completed(val message: ChatMessage) : ChatResponseState()

    data class Failed(val error: Throwable) : ChatResponseState()
} 