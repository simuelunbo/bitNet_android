package com.simuel.onebitllm.data.repository

import com.simuel.onebitllm.BitnetNative
import com.simuel.onebitllm.data.datasource.BitnetNativeDataSource
import com.simuel.onebitllm.data.model.ModelLoadProgressDto
import com.simuel.onebitllm.domain.model.ErrorCode
import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.repository.BitnetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitnetRepositoryImpl @Inject constructor(
    private val bitnetDataSource: BitnetNativeDataSource
) : BitnetRepository {

    override fun loadModel(): Flow<OperationResult<Float>> {
        val progressFlow: Flow<ModelLoadProgressDto> = bitnetDataSource.loadModel()
        return progressFlow.map { progress ->
            try {
                val progressPercentage = progress.bytesLoaded.toFloat() / progress.totalBytes.toFloat()
                OperationResult.Success<Float>(progressPercentage)
            } catch (e: Exception) {
                OperationResult.Failure(
                    errorCode = ErrorCode.PROCESSING_ERROR,
                    message = "모델 로딩 중 오류 발생: ${e.message}",
                    exception = e
                )
            }
        }
    }

    override fun setSystemPrompt(prompt: String): OperationResult<Unit> {
        return try {
            bitnetDataSource.setSystemPrompt(prompt)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            val errorCode = when {
                !BitnetNative.isModelLoaded() -> ErrorCode.MODEL_NOT_LOADED
                else -> ErrorCode.PROCESSING_ERROR
            }
            OperationResult.Failure(
                errorCode = errorCode,
                message = "시스템 프롬프트 설정 실패: ${e.message}",
                exception = e
            )
        }
    }

    override fun setUserPrompt(prompt: String): OperationResult<Unit> {
        return try {
            bitnetDataSource.setUserPrompt(prompt)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            val errorCode = when {
                !BitnetNative.isModelLoaded() -> ErrorCode.MODEL_NOT_LOADED
                else -> ErrorCode.PROCESSING_ERROR
            }
            OperationResult.Failure(
                errorCode = errorCode,
                message = "사용자 프롬프트 설정 실패: ${e.message}",
                exception = e
            )
        }
    }

    override fun generateResponse(userInput: String): Flow<String> {
        // 사용자 프롬프트 설정
        val promptResult = setUserPrompt(userInput)
        if (promptResult is OperationResult.Failure) {
            throw promptResult.exception ?: RuntimeException(promptResult.message)
        }

        // 응답 생성 Flow 반환
        return bitnetDataSource.generateResponseFlow()
    }

    override fun isModelLoaded(): Boolean {
        return BitnetNative.isModelLoaded()
    }

    override fun releaseResources() {
        bitnetDataSource.release()
    }
}