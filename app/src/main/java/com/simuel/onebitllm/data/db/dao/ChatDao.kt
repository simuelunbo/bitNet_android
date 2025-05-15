package com.simuel.onebitllm.data.db.dao

import androidx.room.*
import com.simuel.onebitllm.data.db.entities.Chat
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY updatedAt DESC")
    fun getAllChats(): Flow<List<Chat>>
    
    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: Long): Chat?
    
    @Insert
    suspend fun insertChat(chat: Chat): Long
    
    @Update
    suspend fun updateChat(chat: Chat)
    
    @Query("UPDATE chats SET title = :title, updatedAt = :updatedAt WHERE id = :chatId")
    suspend fun updateChatTitle(chatId: Long, title: String, updatedAt: Date = Date())
    
    @Delete
    suspend fun deleteChat(chat: Chat)
    
    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()
}