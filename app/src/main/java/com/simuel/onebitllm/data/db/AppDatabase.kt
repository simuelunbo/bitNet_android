package com.simuel.onebitllm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simuel.onebitllm.data.db.dao.ChatDao
import com.simuel.onebitllm.data.db.dao.MessageDao
import com.simuel.onebitllm.data.db.entities.ChatEntity
import com.simuel.onebitllm.data.db.entities.MessageEntity
import com.simuel.onebitllm.data.db.util.DateConverter

@Database(
    entities = [ChatEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}