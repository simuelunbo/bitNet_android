package com.simuel.onebitllm.ui.model

import com.simuel.onebitllm.domain.model.ChatMessage

sealed class ChatResponseState {
    data object Started : ChatResponseState()

    data class TokenReceived(
        val token: String,
        val currentContent: String
    ) : ChatResponseState()

    data class Completed(val message: ChatMessage) : ChatResponseState()

    data class Failed(val error: Throwable) : ChatResponseState()
} 