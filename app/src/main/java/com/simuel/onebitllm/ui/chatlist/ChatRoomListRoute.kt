package com.simuel.onebitllm.ui.chatlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simuel.onebitllm.ui.model.ChatRoomListEffect
import com.simuel.onebitllm.ui.model.ChatRoomListEvent

@Composable
fun ChatRoomListRoute(
    onNavigateChat: (Long) -> Unit,
    viewModel: ChatRoomListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is ChatRoomListEffect.NavigateToChat) {
                onNavigateChat(effect.id)
            }
        }
    }

    ChatRoomListScreen(
        chats = state.items,
        onChatClick = { viewModel.onEvent(ChatRoomListEvent.SelectChat(it.id)) },
        onNewChat = { viewModel.onEvent(ChatRoomListEvent.NewChat) },
    )
}
