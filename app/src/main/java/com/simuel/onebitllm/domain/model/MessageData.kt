package com.simuel.onebitllm.domain.model

import java.util.Date

data class MessageData(
    val id: Long = 0,
    val chatId: Long,
    val content: String,
    val isUser: Boolean,
    val timestamp: Date = Date(),
    val isPending: Boolean = false
)