package com.simuel.onebitllm.ui.model

import com.simuel.onebitllm.domain.model.ErrorCode

sealed interface ModelSyncState {
    data object Initial : ModelSyncState
    data class InProgress(val percentComplete: Float) : ModelSyncState
    data object Completed : ModelSyncState
    data class Failed(val errorCode: ErrorCode) : ModelSyncState
} 
