package com.simuel.onebitllm.domain.model

sealed interface ModelSyncStatus {
    object Initial : ModelSyncStatus
    data class InProgress(val percentComplete: Float) : ModelSyncStatus
    object Completed : ModelSyncStatus
    data class Failed(val errorCode: ErrorCode) : ModelSyncStatus
}
