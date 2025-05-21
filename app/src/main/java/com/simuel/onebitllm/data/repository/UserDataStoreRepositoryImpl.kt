package com.simuel.onebitllm.data.repository

import com.simuel.onebitllm.data.datasource.UserDataStoreDataSource
import com.simuel.onebitllm.domain.model.ChatPreferences
import com.simuel.onebitllm.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStoreRepositoryImpl @Inject constructor(
    private val userDataStoreDataSource: UserDataStoreDataSource
) : UserDataStoreRepository {

    override fun getUserPreferences(): Flow<ChatPreferences> {
        return userDataStoreDataSource.userPreferenceFlow.map { preferencesDto ->
            ChatPreferences(
                systemPrompt = preferencesDto.systemPrompt,
                temperature = preferencesDto.temperature,
                maxTokens = preferencesDto.maxTokens,
                darkTheme = preferencesDto.darkTheme
            )
        }
    }

    override suspend fun updateSystemPrompt(prompt: String) {
        userDataStoreDataSource.updateSystemPrompt(prompt)
    }

    override suspend fun updateTemperature(temperature: Float) {
        userDataStoreDataSource.updateTemperature(temperature)
    }

    override suspend fun updateMaxTokens(maxTokens: Int) {
        userDataStoreDataSource.updateMaxTokens(maxTokens)
    }

    override suspend fun updateDarkTheme(enabled: Boolean) {
        userDataStoreDataSource.updateDarkTheme(enabled)
    }
} 
