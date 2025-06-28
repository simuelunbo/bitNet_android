package com.simuel.onebitllm.ui.chatlist

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.simuel.onebitllm.ui.model.ChatRoomListEffect
import com.simuel.onebitllm.ui.model.ChatRoomListEvent
import kotlinx.coroutines.launch

@Composable
fun ChatRoomListRoute(
    onNavigateChat: (Long) -> Unit,
    viewModel: ChatRoomListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current


    DisposableEffect(Unit) {
        val job = lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is ChatRoomListEffect.NavigateToChat -> onNavigateChat(effect.id)
                        is ChatRoomListEffect.ShowError -> Toast.makeText(
                            context,
                            effect.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        onDispose { job.cancel() }
    }


    ChatRoomListScreen(
        chats = state.items,
        onChatClick = { viewModel.onEvent(ChatRoomListEvent.SelectChat(it.id)) },
        onNewChat = { viewModel.onEvent(ChatRoomListEvent.NewChat) },
        onChatDelete = { viewModel.onEvent(ChatRoomListEvent.RemoveChat(it.id)) },
        onChatRename = { chat, title ->
            viewModel.onEvent(ChatRoomListEvent.UpdateTitle(chat.id, title))
        }
    )
}
