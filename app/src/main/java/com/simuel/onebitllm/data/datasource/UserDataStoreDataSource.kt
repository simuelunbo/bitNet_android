package com.simuel.onebitllm.data.datasource

import com.simuel.onebitllm.data.model.UserPreferencesDto
import kotlinx.coroutines.flow.Flow

interface UserDataStoreDataSource {
    val userPreferenceFlow: Flow<UserPreferencesDto>

    suspend fun updateSystemPrompt(systemPrompt: String)

    suspend fun updateTemperature(temperature: Float)

    suspend fun updateMaxTokens(maxTokens: Int)

    suspend fun updateDarkTheme(enabled: Boolean)
}
