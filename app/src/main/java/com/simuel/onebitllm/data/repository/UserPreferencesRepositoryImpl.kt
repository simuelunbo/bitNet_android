package com.simuel.onebitllm.data.repository

import com.simuel.onebitllm.data.datasource.UserPreferencesDataSource
import com.simuel.onebitllm.domain.model.ChatPreferences
import com.simuel.onebitllm.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : UserPreferencesRepository {

    override fun getUserPreferences(): Flow<ChatPreferences> {
        return userPreferencesDataSource.userPreferenceFlow.map { preferencesDto ->
            ChatPreferences(
                systemPrompt = preferencesDto.systemPrompt,
                temperature = preferencesDto.temperature,
                maxTokens = preferencesDto.maxTokens,
                darkTheme = preferencesDto.darkTheme
            )
        }
    }

    override suspend fun updateSystemPrompt(prompt: String) {
        userPreferencesDataSource.updateSystemPrompt(prompt)
    }

    override suspend fun updateTemperature(temperature: Float) {
        userPreferencesDataSource.updateTemperature(temperature)
    }

    override suspend fun updateMaxTokens(maxTokens: Int) {
        userPreferencesDataSource.updateMaxTokens(maxTokens)
    }

    override suspend fun updateDarkTheme(enabled: Boolean) {
        userPreferencesDataSource.updateDarkTheme(enabled)
    }
} 