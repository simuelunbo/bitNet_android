package com.simuel.onebitllm.data.datasource

import com.simuel.onebitllm.data.db.dao.ChatDao
import com.simuel.onebitllm.data.db.dao.MessageDao
import com.simuel.onebitllm.data.db.entities.ChatEntity
import com.simuel.onebitllm.data.db.entities.ChatWithLastMessage
import com.simuel.onebitllm.data.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

class ChatDataSourceImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ChatDataSource {

    override fun getAllChats(): Flow<List<ChatEntity>> {
        return chatDao.getAllChats()
    }

    override fun getChatsWithLastMessage(): Flow<List<ChatWithLastMessage>> {
        return chatDao.getChatsWithLastMessage()
    }
    
    override fun getMessagesByChatId(chatId: Long): Flow<List<MessageEntity>> {
        return messageDao.getMessagesByChatId(chatId)
    }
    
    override suspend fun getChatById(chatId: Long): ChatEntity? {
        return chatDao.getChatById(chatId)
    }
    
    override suspend fun insertChat(title: String): Long {
        val chatEntity = ChatEntity(
            title = title,
            createdAt = Date(),
            updatedAt = Date()
        )
        return chatDao.insertChat(chatEntity)
    }
    
    override suspend fun updateChatTitle(chatId: Long, title: String) {
        chatDao.updateChatTitle(chatId, title)
    }
    
    override suspend fun deleteChat(chatId: Long) {
        val chatEntity = getChatById(chatId)
        chatEntity?.let {
            chatDao.deleteChat(it)
        }
    }
    
    override suspend fun insertUserMessage(chatId: Long, content: String): Long {
        val messageEntity = MessageEntity(
            chatId = chatId,
            content = content,
            isUser = true,
            timestamp = Date()
        )
        return messageDao.insertMessage(messageEntity)
    }
    
    override suspend fun insertAssistantMessage(chatId: Long, initialContent: String): Long {
        val messageEntity = MessageEntity(
            chatId = chatId,
            content = initialContent,
            isUser = false,
            timestamp = Date()
        )
        return messageDao.insertMessage(messageEntity)
    }
    
    override suspend fun updateMessageContent(messageId: Long, content: String) {
        messageDao.updateMessageContent(messageId, content)
    }

    override suspend fun updateChatUpdatedAt(chatId: Long) {
        chatDao.updateChatUpdatedAt(chatId)
    }
} 
