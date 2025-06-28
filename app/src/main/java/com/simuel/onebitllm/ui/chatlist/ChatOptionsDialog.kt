package com.simuel.onebitllm.ui.chatlist

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme

@Composable
fun ChatOptionsDialog(
    chat: ChatRoomItemUiState,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
        title = { Text(chat.name) },
        text = {
            Column {
                TextButton(onClick = onRename) { Text("제목 수정") }
                TextButton(onClick = onDelete) { Text("삭제") }
            }
        }
    )
}

@Composable
fun RenameChatDialog(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("수정") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
        title = { Text("제목 수정") },
        text = {
            TextField(
                value = text,
                onValueChange = onTextChange,
                singleLine = true
            )
        }
    )
}

@Composable
fun DeleteChatDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
        text = { Text("삭제 하겠습니까?") }
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatOptionsDialogPreview() {
    OnebitLLMTheme {
        ChatOptionsDialog(
            chat = ChatRoomItemUiState(1, "Title", ""),
            onRename = {},
            onDelete = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RenameChatDialogPreview() {
    OnebitLLMTheme {
        RenameChatDialog(
            text = TextFieldValue("Title", selection = TextRange(0, 5)),
            onTextChange = {},
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteChatDialogPreview() {
    OnebitLLMTheme {
        DeleteChatDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
