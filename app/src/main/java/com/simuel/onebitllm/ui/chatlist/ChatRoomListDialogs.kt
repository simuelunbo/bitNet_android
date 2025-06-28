package com.simuel.onebitllm.ui.chatlist

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.model.DialogState
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme

@Composable
fun ChatRoomListDialogs(
    state: DialogState,
    onStateChange: (DialogState) -> Unit,
    onChatDelete: (ChatRoomItemUiState) -> Unit,
    onChatRename: (ChatRoomItemUiState, String) -> Unit
) {
    when (state) {
        is DialogState.Options -> {
            ChatOptionsDialog(
                chat = state.chat,
                onRename = {
                    onStateChange(
                        DialogState.Rename(
                            state.chat,
                            TextFieldValue(
                                state.chat.name,
                                selection = TextRange(0, state.chat.name.length)
                            )
                        )
                    )
                },
                onDelete = { onStateChange(DialogState.ConfirmDelete(state.chat)) },
                onDismiss = { onStateChange(DialogState.None) }
            )
        }

        is DialogState.ConfirmDelete -> {
            DeleteChatDialog(
                onConfirm = {
                    onChatDelete(state.chat)
                    onStateChange(DialogState.None)
                },
                onDismiss = { onStateChange(DialogState.None) }
            )
        }

        is DialogState.Rename -> {
            RenameChatDialog(
                text = state.text,
                onTextChange = { onStateChange(DialogState.Rename(state.chat, it)) },
                onConfirm = {
                    onChatRename(state.chat, state.text.text)
                    onStateChange(DialogState.None)
                },
                onDismiss = { onStateChange(DialogState.None) }
            )
        }

        DialogState.None -> {}
    }
}

@Preview(showBackground = true, name = "Options")
@Composable
private fun ChatRoomListDialogsOptionsPreview() {
    val chat = ChatRoomItemUiState(1, "Sample Chat", "Last message")
    OnebitLLMTheme {
        ChatRoomListDialogs(
            state = DialogState.Options(chat),
            onStateChange = {},
            onChatDelete = {},
            onChatRename = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "Rename")
@Composable
private fun ChatRoomListDialogsRenamePreview() {
    val chat = ChatRoomItemUiState(1, "Sample Chat", "Last message")
    OnebitLLMTheme {
        ChatRoomListDialogs(
            state = DialogState.Rename(
                chat,
                TextFieldValue("Sample Chat", selection = TextRange(0, 11))
            ),
            onStateChange = {},
            onChatDelete = {},
            onChatRename = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "Confirm Delete")
@Composable
private fun ChatRoomListDialogsConfirmDeletePreview() {
    val chat = ChatRoomItemUiState(1, "Sample Chat", "Last message")
    OnebitLLMTheme {
        ChatRoomListDialogs(
            state = DialogState.ConfirmDelete(chat),
            onStateChange = {},
            onChatDelete = {},
            onChatRename = { _, _ -> }
        )
    }
}
