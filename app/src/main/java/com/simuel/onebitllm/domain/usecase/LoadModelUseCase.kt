package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.repository.BitnetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadModelUseCase @Inject constructor(
    private val repository: BitnetRepository
){
    operator fun invoke(): Flow<OperationResult<Float>> = repository.loadModel()
}