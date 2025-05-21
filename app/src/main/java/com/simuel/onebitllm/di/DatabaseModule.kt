package com.simuel.onebitllm.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.simuel.onebitllm.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = databaseBuilder(
        context, AppDatabase::class.java, "news_db"
    ).fallbackToDestructiveMigration(dropAllTables = false)
        .build()

    @Singleton
    @Provides
    fun provideChatDao(db: AppDatabase) = db.chatDao()

    @Singleton
    @Provides
    fun provideMessageDao(db: AppDatabase) = db.messageDao()
}
