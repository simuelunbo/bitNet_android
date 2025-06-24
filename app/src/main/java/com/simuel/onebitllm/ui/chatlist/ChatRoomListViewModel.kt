package com.simuel.onebitllm.ui.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simuel.onebitllm.domain.usecase.ChatListUseCases
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.model.ChatRoomListEffect
import com.simuel.onebitllm.ui.model.ChatRoomListEvent
import com.simuel.onebitllm.ui.model.ChatRoomListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomListViewModel @Inject constructor(
    private val useCases: ChatListUseCases,
) : ViewModel() {

    val state: StateFlow<ChatRoomListState> = useCases.getChatSummaries().map { list ->
        ChatRoomListState(items = list.map { chatSummary ->
            ChatRoomItemUiState(
                id = chatSummary.id,
                name = chatSummary.title,
                lastMessage = chatSummary.lastMessage.orEmpty(),
            )
        })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatRoomListState()
    )

    private val _effect = Channel<ChatRoomListEffect>(Channel.BUFFERED)
    val effect: Flow<ChatRoomListEffect> = _effect.receiveAsFlow()

    fun onEvent(event: ChatRoomListEvent) {
        when (event) {
            ChatRoomListEvent.NewChat -> handleCreateChat()
            is ChatRoomListEvent.SelectChat -> _effect.trySend(
                ChatRoomListEffect.NavigateToChat(
                    event.id
                )
            )

            is ChatRoomListEvent.RemoveChat -> handleDeleteChat(event.id)
        }
    }

    private fun handleCreateChat() {
        viewModelScope.launch {
            runCatching { useCases.createChat() }
                .onSuccess { id ->
                    _effect.send(ChatRoomListEffect.NavigateToChat(id))
                }
                .onFailure { e ->
                    _effect.send(
                        ChatRoomListEffect.ShowError(e.message ?: "알 수 없는 오류")
                    )
                }
        }
    }

    private fun handleDeleteChat(id: Long) {
        viewModelScope.launch {
            runCatching { useCases.deleteChat(id) }
                .onFailure { e ->
                    _effect.send(
                        ChatRoomListEffect.ShowError(e.message ?: "알 수 없는 오류")
                    )
                }
        }
    }
}
