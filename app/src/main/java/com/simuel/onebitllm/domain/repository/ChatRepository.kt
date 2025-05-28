package com.simuel.onebitllm.domain.repository

import com.simuel.onebitllm.domain.model.Chat
import com.simuel.onebitllm.domain.model.ChatMessage
import com.simuel.onebitllm.domain.model.ChatSummary
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllChats(): Flow<List<Chat>>
    fun getChatsWithLastMessage(): Flow<List<ChatSummary>>
    fun getMessagesByChatId(chatId: Long): Flow<List<ChatMessage>>
    suspend fun createNewChat(title: String): Long
    suspend fun updateChatTitle(chatId: Long, title: String)
    suspend fun deleteChat(chatId: Long)
    suspend fun addUserMessage(chatId: Long, content: String): Long
    suspend fun addAssistantMessage(chatId: Long, initialContent: String = ""): Long
    suspend fun updateMessageContent(messageId: Long, content: String)
//    suspend fun getMessageById(messageId: Long): ChatMessage
}
