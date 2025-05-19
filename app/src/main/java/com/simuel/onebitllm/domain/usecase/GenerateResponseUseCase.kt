package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.ChatMessage
import com.simuel.onebitllm.domain.model.ErrorCode
import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.repository.BitnetRepository
import com.simuel.onebitllm.domain.repository.ChatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.Date

/**
 * AI 응답 생성을 담당하는 유스케이스
 * 
 * 1. 사용자 메시지를 저장
 * 2. AI 응답을 위한 빈 메시지 생성
 * 3. AI 응답을 토큰 단위로 생성하며 실시간으로 메시지 업데이트
 * 4. 최종 응답 메시지 반환
 */
class GenerateResponseUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val bitnetRepository: BitnetRepository
) {
    /**
     * AI 응답 생성 실행
     * @param chatId 채팅방 ID
     * @param userInput 사용자 입력 메시지
     * @return Flow<OperationResult<ChatMessage>> 응답 생성 결과
     */
    suspend operator fun invoke(chatId: Long, userInput: String): Flow<OperationResult<ChatMessage>> {
        // 1. 사용자 메시지 저장
        chatRepository.addUserMessage(chatId, userInput)
        
        // 2. AI 응답을 위한 빈 메시지 생성
        val assistantMessageId = chatRepository.addAssistantMessage(chatId)

        return flow {
            try {
                // 3. AI 응답 생성 및 실시간 업데이트
                val responseContent = generateAndUpdateResponse(assistantMessageId, userInput)
                
                // 4. 최종 응답 메시지 생성 및 반환
                val finalMessage = createAssistantMessage(
                    id = assistantMessageId,
                    chatId = chatId,
                    content = responseContent
                )
                emit(OperationResult.Success(finalMessage))
            } catch (e: Exception) {
                handleError(assistantMessageId, e, ErrorCode.PROCESSING_ERROR)
            }
        }.catch { e ->
            handleError(assistantMessageId, e, ErrorCode.UNKNOWN_ERROR)
        }
    }

    /**
     * AI 응답을 생성하고 실시간으로 업데이트
     * @return 생성된 전체 응답 내용
     */
    private suspend fun generateAndUpdateResponse(
        messageId: Long,
        userInput: String
    ): String {
        val stringBuilder = StringBuilder()
        
        bitnetRepository.generateResponse(userInput).collect { token ->
            stringBuilder.append(token)
            val currentContent = stringBuilder.toString()
            chatRepository.updateMessageContent(messageId, currentContent)
        }
        
        return stringBuilder.toString()
    }

    /**
     * 어시스턴트 메시지 객체 생성
     */
    private fun createAssistantMessage(
        id: Long,
        chatId: Long,
        content: String
    ): ChatMessage = ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        isUser = false,
        timestamp = Date(),
        isPending = false
    )

    /**
     * 에러 처리 및 에러 메시지 반환
     */
    private suspend fun FlowCollector<OperationResult<ChatMessage>>.handleError(
        messageId: Long,
        error: Throwable,
        errorCode: ErrorCode
    ) {
        val errorMessage = when (errorCode) {
            ErrorCode.PROCESSING_ERROR -> "응답 생성 중 오류 발생: ${error.message}"
            else -> "처리 중 예외 발생: ${error.message}"
        }
        
        chatRepository.updateMessageContent(messageId, errorMessage)
        emit(OperationResult.Failure(
            errorCode = errorCode,
            message = errorMessage,
            exception = error
        ))
    }
}
