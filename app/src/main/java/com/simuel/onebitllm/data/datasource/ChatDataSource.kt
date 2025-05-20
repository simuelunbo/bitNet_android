package com.simuel.onebitllm.data.datasource

import com.simuel.onebitllm.data.db.entities.ChatEntity
import com.simuel.onebitllm.data.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {
    fun getAllChats(): Flow<List<ChatEntity>>
    
    fun getMessagesByChatId(chatId: Long): Flow<List<MessageEntity>>
    
    suspend fun getChatById(chatId: Long): ChatEntity?
    
    suspend fun insertChat(title: String): Long
    
    suspend fun updateChatTitle(chatId: Long, title: String)
    
    suspend fun deleteChat(chatId: Long)
    
    suspend fun insertUserMessage(chatId: Long, content: String): Long
    
    suspend fun insertAssistantMessage(chatId: Long, initialContent: String): Long
    
    suspend fun updateMessageContent(messageId: Long, content: String)
} 