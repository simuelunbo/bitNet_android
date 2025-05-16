package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.model.ModelSyncStatus
import com.simuel.onebitllm.domain.repository.BitnetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadModelUseCase @Inject constructor(
    private val bitnetRepository: BitnetRepository
) {
    operator fun invoke(): Flow<ModelSyncStatus> {
        return bitnetRepository.loadModel()
    }
}