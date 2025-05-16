package com.simuel.onebitllm.domain.model

import java.util.Date

data class ChatData(
    val id: Long = 0,
    val title: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)