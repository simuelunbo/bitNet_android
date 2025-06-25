package com.simuel.onebitllm.domain.usecase

import javax.inject.Inject

data class ChatUseCases @Inject constructor(
    val getChatMessages: GetChatMessagesUseCase,
    val generateResponse: GenerateResponseUseCase,
    val updateChatTitle: UpdateChatTitleUseCase,
)
