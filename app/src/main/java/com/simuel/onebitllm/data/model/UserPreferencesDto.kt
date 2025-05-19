package com.simuel.onebitllm.data.model

data class UserPreferencesDto(
    val systemPrompt: String,
    val temperature: Float,
    val maxTokens: Int,
    val darkTheme: Boolean
)
