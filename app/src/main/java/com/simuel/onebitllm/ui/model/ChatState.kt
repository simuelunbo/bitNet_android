package com.simuel.onebitllm.ui.model

import com.simuel.onebitllm.domain.model.ChatMessage

/** 채팅 화면 상태 */
data class ChatState(
    val messages: List<ChatMessage> = emptyList()
)
