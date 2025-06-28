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
import com.simuel.onebitllm.ui.theme.BackgroundColor
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme
import com.simuel.onebitllm.ui.theme.TitleColor

private sealed interface DialogState {
    data object None : DialogState
    data class Options(val chat: ChatRoomItemUiState) : DialogState
    data class ConfirmDelete(val chat: ChatRoomItemUiState) : DialogState
    data class Rename(val chat: ChatRoomItemUiState, val text: TextFieldValue) : DialogState
}

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.size(48.dp))
            Text(
                text = "Chats",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TitleColor
            )
            IconButton(onClick = onNewChat) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Chat",
                    tint = TitleColor
                )
            }
        }

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

        when (val state = dialogState) {
            is DialogState.Options -> {
                ChatOptionsDialog(
                    chat = state.chat,
                    onRename = {
                        dialogState = DialogState.Rename(
                            state.chat,
                            TextFieldValue(
                                state.chat.name,
                                selection = TextRange(0, state.chat.name.length)
                            )
                        )
                    },
                    onDelete = { dialogState = DialogState.ConfirmDelete(state.chat) },
                    onDismiss = { dialogState = DialogState.None }
                )
            }

            is DialogState.ConfirmDelete -> {
                DeleteChatDialog(
                    onConfirm = {
                        onChatDelete(state.chat)
                        dialogState = DialogState.None
                    },
                    onDismiss = { dialogState = DialogState.None }
                )
            }

            is DialogState.Rename -> {
                RenameChatDialog(
                    text = state.text,
                    onTextChange = { dialogState = state.copy(text = it) },
                    onConfirm = {
                        onChatRename(state.chat, state.text.text)
                        dialogState = DialogState.None
                    },
                    onDismiss = { dialogState = DialogState.None }
                )
            }

            DialogState.None -> {}

        }
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
