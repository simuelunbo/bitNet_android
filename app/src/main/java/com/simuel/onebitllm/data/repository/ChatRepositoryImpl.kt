package com.simuel.onebitllm.data.repository

import com.simuel.onebitllm.data.datasource.ChatDataSource
import com.simuel.onebitllm.domain.model.Chat
import com.simuel.onebitllm.domain.model.ChatMessage
import com.simuel.onebitllm.domain.model.ChatSummary
import com.simuel.onebitllm.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {

    override fun getAllChats(): Flow<List<Chat>> {
        return chatDataSource.getAllChats().map { chatEntities ->
            chatEntities.map { entity ->
                Chat(
                    id = entity.id,
                    title = entity.title,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt
                )
            }
        }
    }

    override fun getChatsWithLastMessage(): Flow<List<ChatSummary>> {
        return chatDataSource.getChatsWithLastMessage().map { rows ->
            rows.map { row ->
                ChatSummary(
                    id = row.id,
                    title = row.title,
                    lastMessage = row.lastMessage
                )
            }
        }
    }

    override fun getMessagesByChatId(chatId: Long): Flow<List<ChatMessage>> {
        return chatDataSource.getMessagesByChatId(chatId).map { messageEntities ->
            messageEntities.map { entity ->
                ChatMessage(
                    id = entity.id,
                    chatId = entity.chatId,
                    content = entity.content,
                    isUser = entity.isUser,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    override suspend fun createNewChat(title: String): Long {
        return chatDataSource.insertChat(title)
    }

    override suspend fun updateChatTitle(chatId: Long, title: String) {
        chatDataSource.updateChatTitle(chatId, title)
    }

    override suspend fun deleteChat(chatId: Long) {
        chatDataSource.deleteChat(chatId)
    }

    override suspend fun addUserMessage(chatId: Long, content: String): Long {
        val id = chatDataSource.insertUserMessage(chatId, content)
        chatDataSource.updateChatUpdatedAt(chatId)
        return id
    }

    override suspend fun addAssistantMessage(chatId: Long, initialContent: String): Long {
        val id = chatDataSource.insertAssistantMessage(chatId, initialContent)
        chatDataSource.updateChatUpdatedAt(chatId)
        return id
    }

    override suspend fun updateMessageContent(messageId: Long, content: String) {
        chatDataSource.updateMessageContent(messageId, content)
    }
    
} 
