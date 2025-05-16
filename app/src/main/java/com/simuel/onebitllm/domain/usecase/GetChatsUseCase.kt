package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.ChatData
import com.simuel.onebitllm.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatData>> {
        return chatRepository.getAllChats()
    }
}