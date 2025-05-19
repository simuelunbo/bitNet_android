package com.simuel.onebitllm.data.db.dao

import androidx.room.*
import com.simuel.onebitllm.data.db.entities.ChatEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY updatedAt DESC")
    fun getAllChats(): Flow<List<ChatEntity>>
    
    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: Long): ChatEntity?
    
    @Insert
    suspend fun insertChat(chatEntity: ChatEntity): Long
    
    @Update
    suspend fun updateChat(chatEntity: ChatEntity)
    
    @Query("UPDATE chats SET title = :title, updatedAt = :updatedAt WHERE id = :chatId")
    suspend fun updateChatTitle(chatId: Long, title: String, updatedAt: Date = Date())
    
    @Delete
    suspend fun deleteChat(chatEntity: ChatEntity)
    
    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()
}