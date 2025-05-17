package com.simuel.onebitllm.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.simuel.onebitllm.domain.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val SYSTEM_PROMPT = stringPreferencesKey("system_prompt")
        val TEMPERATURE = floatPreferencesKey("temperature")
        val MAX_TOKENS = intPreferencesKey("max_tokens")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    private val defaultSystemPrompt = """
You are a small, lightweight but powerful AI assistant OneBit.
Please provide a kind and accurate answer to the user.
Answer succinctly, but deliver all the information you need.
    """.trimIndent()

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val systemPrompt = preferences[PreferencesKeys.SYSTEM_PROMPT] ?: defaultSystemPrompt
        val temperature = preferences[PreferencesKeys.TEMPERATURE] ?: 0.7f
        val maxTokens = preferences[PreferencesKeys.MAX_TOKENS] ?: 1024
        val darkTheme = preferences[PreferencesKeys.DARK_THEME] ?: false

        UserPreferences(
            systemPrompt = systemPrompt,
            temperature = temperature,
            maxTokens = maxTokens,
            darkTheme = darkTheme
        )
    }

    suspend fun updateSystemPrompt(systemPrompt: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SYSTEM_PROMPT] = systemPrompt
        }
    }

    suspend fun updateTemperature(temperature: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEMPERATURE] = temperature
        }
    }

    suspend fun updateMaxTokens(maxTokens: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_TOKENS] = maxTokens
        }
    }

    suspend fun updateDarkTheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = enabled
        }
    }
}
