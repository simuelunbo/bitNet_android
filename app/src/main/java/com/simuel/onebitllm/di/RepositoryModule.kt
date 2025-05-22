package com.simuel.onebitllm.di

import com.simuel.onebitllm.data.repository.BitnetRepositoryImpl
import com.simuel.onebitllm.data.repository.ChatRepositoryImpl
import com.simuel.onebitllm.data.repository.SettingsRepositoryImpl
import com.simuel.onebitllm.domain.repository.BitnetRepository
import com.simuel.onebitllm.domain.repository.ChatRepository
import com.simuel.onebitllm.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindBitnetRepository(repo: BitnetRepositoryImpl): BitnetRepository
    @Binds
    abstract fun bindChatRepository(repo: ChatRepositoryImpl): ChatRepository
    @Binds
    abstract fun bindSettingsRepository(repo: SettingsRepositoryImpl): SettingsRepository
}