package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.MessageData
import com.simuel.onebitllm.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: Long): Flow<List<MessageData>> {
        return chatRepository.getMessagesByChatId(chatId)
    }
}