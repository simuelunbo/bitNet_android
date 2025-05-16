package com.simuel.onebitllm.domain.repository

import com.simuel.onebitllm.domain.model.ChatData
import com.simuel.onebitllm.domain.model.MessageData
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllChats(): Flow<List<ChatData>>
    fun getMessagesByChatId(chatId: Long): Flow<List<MessageData>>
    suspend fun createNewChat(title: String): Long
    suspend fun updateChatTitle(chatId: Long, title: String)
    suspend fun deleteChat(chatId: Long)
    suspend fun addUserMessage(chatId: Long, content: String): Long
    suspend fun addAssistantMessage(chatId: Long, initialContent: String = ""): Long
    suspend fun updateMessageContent(messageId: Long, content: String)
}