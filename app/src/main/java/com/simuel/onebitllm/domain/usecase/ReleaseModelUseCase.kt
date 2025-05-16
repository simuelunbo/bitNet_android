package com.simuel.onebitllm.domain.usecase

import com.simuel.onebitllm.domain.repository.BitnetRepository
import javax.inject.Inject

class ReleaseModelUseCase @Inject constructor(
    private val bitnetRepository: BitnetRepository
) {
    operator fun invoke() {
        bitnetRepository.releaseResources()
    }
}