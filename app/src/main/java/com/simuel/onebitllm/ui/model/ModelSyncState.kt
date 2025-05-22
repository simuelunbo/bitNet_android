package com.simuel.onebitllm.ui.model

import com.simuel.onebitllm.domain.model.ErrorCode

sealed interface ModelSyncState {
    object Initial : ModelSyncState
    data class InProgress(val percentComplete: Float) : ModelSyncState
    object Completed : ModelSyncState
    data class Failed(val errorCode: ErrorCode) : ModelSyncState
} 