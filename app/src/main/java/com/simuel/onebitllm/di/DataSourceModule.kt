package com.simuel.onebitllm.di

import com.simuel.onebitllm.data.datasource.ChatDataSource
import com.simuel.onebitllm.data.datasource.ChatDataSourceImpl
import com.simuel.onebitllm.data.datasource.UserDataStoreDataSource
import com.simuel.onebitllm.data.datasource.UserDataStoreDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindUserDataStoreDataSource(source: UserDataStoreDataSourceImpl): UserDataStoreDataSource

    @Binds
    abstract fun bindChatDataSourceImpl(source: ChatDataSourceImpl): ChatDataSource
}
