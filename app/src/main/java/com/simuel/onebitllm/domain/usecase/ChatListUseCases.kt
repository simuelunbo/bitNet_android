package com.simuel.onebitllm.domain.usecase

import javax.inject.Inject

/**
 * Chat 목록 화면에서 사용되는 유스케이스 모음
 */
data class ChatListUseCases @Inject constructor(
    val getChatSummaries: GetChatSummariesUseCase,
    val createChat: CreateChatUseCase,
    val deleteChat: DeleteChatUseCase,
)
