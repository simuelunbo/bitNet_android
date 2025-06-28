package com.simuel.onebitllm.ui.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.model.DialogState
import com.simuel.onebitllm.ui.theme.BackgroundColor
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme
import com.simuel.onebitllm.ui.theme.TitleColor


@Composable
fun ChatRoomListScreen(
    chats: List<ChatRoomItemUiState>,
    onNewChat: () -> Unit,
    onChatClick: (ChatRoomItemUiState) -> Unit,
    onChatDelete: (ChatRoomItemUiState) -> Unit,
    onChatRename: (ChatRoomItemUiState, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogState by remember { mutableStateOf<DialogState>(DialogState.None) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        ChatRoomListTopBar(onNewChat = onNewChat)

        if (chats.isEmpty()) {
            ChatRoomListEmptyState(
                onNewChat = onNewChat, modifier = Modifier.weight(1f)
            )
        } else {
            ChatRoomListContent(
                chats = chats, onItemClick = onChatClick, onItemLongClick = {
                    dialogState = DialogState.Options(it)
                }, modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        ChatRoomListDialogs(
            state = dialogState,
            onStateChange = { dialogState = it },
            onChatDelete = onChatDelete,
            onChatRename = onChatRename
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatListScreenPreview() {
    OnebitLLMTheme {
        ChatRoomListScreen(
            sampleChats(),
            onNewChat = {},
            onChatClick = {},
            onChatDelete = {},
            onChatRename = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyChatListScreenPreview() {
    ChatRoomListScreen(
        emptyList(),
        onNewChat = {},
        onChatClick = {},
        onChatDelete = {},
        onChatRename = { _, _ -> })
}

private fun sampleChats(): List<ChatRoomItemUiState> = listOf(
    ChatRoomItemUiState(1, "Title 1", "Hey, how are you?"),
    ChatRoomItemUiState(2, "Title 2", "I'm on my way"),
    ChatRoomItemUiState(3, "Title 3", "See you soon"),
    ChatRoomItemUiState(4, "Title 4", "I'm here"),
)
