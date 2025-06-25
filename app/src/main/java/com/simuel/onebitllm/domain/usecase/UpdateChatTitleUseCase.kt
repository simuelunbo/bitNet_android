package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.repository.ChatRepository
import javax.inject.Inject

class UpdateChatTitleUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: Long, title: String) {
        chatRepository.updateChatTitle(chatId, title)
    }
}
