package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.ChatSummary
import com.simuel.onebitllm.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatSummariesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatSummary>> {
        return chatRepository.getChatsWithLastMessage()
    }
}
