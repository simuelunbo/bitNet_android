package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.ErrorCode
import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.model.ResponseGenerationState
import com.simuel.onebitllm.domain.repository.BitnetRepository
import com.simuel.onebitllm.domain.repository.ChatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GenerateResponseUseCase @Inject constructor(
    private val chatRepository: ChatRepository, private val bitnetRepository: BitnetRepository
) {
    suspend operator fun invoke(chatId: Long, userInput: String): Flow<ResponseGenerationState> {
        chatRepository.addUserMessage(chatId, userInput)
        val assistantMessageId = chatRepository.addAssistantMessage(chatId)

        return flow {
            emit(ResponseGenerationState.Started(assistantMessageId))

            val stringBuilder = StringBuilder()

            try {
                bitnetRepository.generateResponse(userInput).collect { token ->
                    stringBuilder.append(token)
                    val currentContent = stringBuilder.toString()

                    chatRepository.updateMessageContent(assistantMessageId, currentContent)

                    emit(ResponseGenerationState.InProgress(assistantMessageId, currentContent))
                }

                emit(
                    ResponseGenerationState.Completed(
                        assistantMessageId, stringBuilder.toString()
                    )
                )
            } catch (e: Exception) {
                val errorMessage = "응답 생성 중 오류 발생: ${e.message}"
                chatRepository.updateMessageContent(assistantMessageId, errorMessage)

                val errorResult = OperationResult.Failure(
                    errorCode = ErrorCode.PROCESSING_ERROR, message = errorMessage, exception = e
                )

                emit(ResponseGenerationState.Error(assistantMessageId, errorResult))
            }
        }.catch { e ->
            val errorMessage = "처리 중 예외 발생: ${e.message}"
            chatRepository.updateMessageContent(assistantMessageId, errorMessage)

            val errorResult = OperationResult.Failure(
                errorCode = ErrorCode.UNKNOWN_ERROR, message = errorMessage, exception = e
            )
            emit(ResponseGenerationState.Error(assistantMessageId, errorResult))
        }
    }
}
