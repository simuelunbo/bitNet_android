package com.simuel.onebitllm.domain.repository

import com.simuel.onebitllm.domain.model.OperationResult
import kotlinx.coroutines.flow.Flow

interface BitnetRepository {
    fun loadModel(): Flow<OperationResult<Float>>
    fun isModelLoaded(): Boolean
    fun setSystemPrompt(prompt: String): OperationResult<Unit>
    fun setUserPrompt(prompt: String): OperationResult<Unit>
    fun generateResponse(userInput: String): Flow<String>
    fun releaseResources()
}