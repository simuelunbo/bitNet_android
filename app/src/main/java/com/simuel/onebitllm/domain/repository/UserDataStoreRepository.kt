package com.simuel.onebitllm.domain.repository

import com.simuel.onebitllm.domain.model.ChatPreferences
import kotlinx.coroutines.flow.Flow

interface UserDataStoreRepository {
    fun getUserPreferences(): Flow<ChatPreferences>
    
    suspend fun updateSystemPrompt(prompt: String)
    suspend fun updateTemperature(temperature: Float)
    suspend fun updateMaxTokens(maxTokens: Int)
    suspend fun updateDarkTheme(enabled: Boolean)
}
