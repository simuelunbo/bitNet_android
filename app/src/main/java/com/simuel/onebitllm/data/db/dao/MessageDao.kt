package com.simuel.onebitllm.data.db.dao

import androidx.room.*
import com.simuel.onebitllm.data.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesByChatId(chatId: Long): Flow<List<MessageEntity>>
    
    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity): Long
    
    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)
    
    @Query("UPDATE messages SET content = :content WHERE id = :messageId")
    suspend fun updateMessageContent(messageId: Long, content: String)
    
    @Delete
    suspend fun deleteMessage(messageEntity: MessageEntity)
    
    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: Long)
}