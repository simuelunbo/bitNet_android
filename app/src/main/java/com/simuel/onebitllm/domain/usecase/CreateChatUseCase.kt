package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.repository.ChatRepository
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(title: String = "새 대화"): Long {
        return chatRepository.createNewChat(title)
    }
}