package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.repository.BitnetRepository
import javax.inject.Inject

class UpdateSystemPromptUseCase @Inject constructor(
    private val bitnetRepository: BitnetRepository
) {
    operator fun invoke(prompt: String): OperationResult<Unit> {
        return bitnetRepository.setSystemPrompt(prompt)
    }
}