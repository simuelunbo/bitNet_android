package com.simuel.onebitllm.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simuel.onebitllm.domain.usecase.GenerateResponseUseCase
import com.simuel.onebitllm.domain.usecase.GetChatMessagesUseCase
import com.simuel.onebitllm.ui.model.ChatResponseState
import com.simuel.onebitllm.ui.model.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatMessages: GetChatMessagesUseCase,
    private val generateResponse: GenerateResponseUseCase,
    ) : ViewModel() {

    private val chatId: Long = checkNotNull(savedStateHandle["id"]) 

    val state: StateFlow<ChatState> = getChatMessages(chatId).map { messages ->
        ChatState(messages)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatState()
    )
    private val _responseState = MutableStateFlow<ChatResponseState>(ChatResponseState.Initial)
    val responseState: StateFlow<ChatResponseState> = _responseState.asStateFlow()

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            generateResponse(chatId, content)
                .onStart { _responseState.value = ChatResponseState.Started }
                .catch { e -> _responseState.value = ChatResponseState.Failed(e) }
                .collect { result ->
                    when (result) {
                        is com.simuel.onebitllm.domain.model.OperationResult.Success ->
                            _responseState.value = ChatResponseState.Completed(result.data)
                        is com.simuel.onebitllm.domain.model.OperationResult.Failure ->
                            _responseState.value = ChatResponseState.Failed(
                                result.exception ?: RuntimeException(result.message)
                            )
                    }
                }
        }
    }
}
