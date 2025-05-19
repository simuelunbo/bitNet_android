package com.simuel.onebitllm.domain.model

data class ChatPreferences(
    val systemPrompt: String,
    val temperature: Float,
    val maxTokens: Int,
    val darkTheme: Boolean
)