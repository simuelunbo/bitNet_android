package com.simuel.onebitllm.ui.model

import androidx.compose.ui.text.input.TextFieldValue

sealed interface DialogState {
    data object None : DialogState
    data class Options(val chat: ChatRoomItemUiState) : DialogState
    data class ConfirmDelete(val chat: ChatRoomItemUiState) : DialogState
    data class Rename(val chat: ChatRoomItemUiState, val text: TextFieldValue) : DialogState
}
