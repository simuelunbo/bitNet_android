package com.simuel.onebitllm.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.simuel.onebitllm.data.model.UserPreferencesDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class UserPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesDataSource {

    override val userPreferenceFlow: Flow<UserPreferencesDto> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val systemPrompt = preferences[SYSTEM_PROMPT] ?: DEFAULT_SYSTEM_PROMPT
            val temperature = preferences[TEMPERATURE] ?: DEFAULT_TEMPERATURE
            val maxTokens = preferences[MAX_TOKENS] ?: DEFAULT_MAX_TOKENS
            val darkTheme = preferences[DARK_THEME] == true

            UserPreferencesDto(
                systemPrompt = systemPrompt,
                temperature = temperature,
                maxTokens = maxTokens,
                darkTheme = darkTheme
            )
        }

    override suspend fun updateSystemPrompt(systemPrompt: String) {
        dataStore.edit { preferences ->
            preferences[SYSTEM_PROMPT] = systemPrompt
        }
    }

    override suspend fun updateTemperature(temperature: Float) {
        dataStore.edit { preferences ->
            preferences[TEMPERATURE] = temperature
        }
    }

    override suspend fun updateMaxTokens(maxTokens: Int) {
        dataStore.edit { preferences ->
            preferences[MAX_TOKENS] = maxTokens
        }
    }

    override suspend fun updateDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME] = enabled
        }
    }

    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
        private const val DEFAULT_TEMPERATURE = 0.7f
        private const val DEFAULT_MAX_TOKENS = 1024

        private val SYSTEM_PROMPT = stringPreferencesKey("system_prompt")
        private val TEMPERATURE = floatPreferencesKey("temperature")
        private val MAX_TOKENS = intPreferencesKey("max_tokens")
        private val DARK_THEME = booleanPreferencesKey("dark_theme")

        private val DEFAULT_SYSTEM_PROMPT = """
            You are a small, lightweight but powerful AI assistant OneBit.
            Please provide a kind and accurate answer to the user.
            Answer succinctly, but deliver all the information you need.
        """.trimIndent()
    }
}
