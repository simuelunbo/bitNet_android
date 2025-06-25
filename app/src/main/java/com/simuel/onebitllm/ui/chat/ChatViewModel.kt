package com.simuel.onebitllm.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simuel.onebitllm.domain.model.ChatMessage
import com.simuel.onebitllm.domain.model.OperationResult
import com.simuel.onebitllm.domain.usecase.ChatUseCases
import com.simuel.onebitllm.ui.model.ChatResponseState
import com.simuel.onebitllm.ui.model.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: ChatUseCases,
) : ViewModel() {

    private val chatId: Long = checkNotNull(savedStateHandle["id"])

    val state: StateFlow<ChatState> = useCases.getChatMessages(chatId).map { messages ->
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
            updateChatTitleIfNeeded(content)
            useCases.generateResponse(chatId, content)
                .onStart { _responseState.value = ChatResponseState.Started }
                .catch { e -> _responseState.value = ChatResponseState.Failed(e) }
                .collect(::handleResult)
        }
    }

    private suspend fun updateChatTitleIfNeeded(content: String) {
        if (state.value.messages.isNotEmpty()) return
        val title = generateTitle(content)
        useCases.updateChatTitle(chatId, title)
    }

    private fun handleResult(result: OperationResult<ChatMessage>) {
        _responseState.value = when (result) {
            is OperationResult.Success -> ChatResponseState.Completed(result.data)
            is OperationResult.Failure -> ChatResponseState.Failed(
                result.exception ?: RuntimeException(result.message)
            )
        }
    }

    private fun generateTitle(content: String): String {
        val firstLine = content.trim().lineSequence().firstOrNull().orEmpty()
        return firstLine.take(20)
    }
}
