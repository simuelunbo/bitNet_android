package com.simuel.onebitllm.domain.model

data class UserPreferences(
    val systemPrompt: String,
    val temperature: Float,
    val maxTokens: Int,
    val darkTheme: Boolean
)