package com.simuel.onebitllm.ui.model

import com.simuel.onebitllm.ui.model.ChatRoomItemUiState

/** 화면 상태 */
data class ChatRoomListState(
    val items: List<ChatRoomItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/** 사용자 이벤트 */
sealed interface ChatRoomListEvent {
    data object NewChat : ChatRoomListEvent
    data class SelectChat(val id: Long) : ChatRoomListEvent
    data class RemoveChat(val id: Long) : ChatRoomListEvent
}

/** 단발성 사이드이펙트 */
sealed interface ChatRoomListEffect {
    data class NavigateToChat(val id: Long) : ChatRoomListEffect
    data class ShowError(val message: String) : ChatRoomListEffect
}
