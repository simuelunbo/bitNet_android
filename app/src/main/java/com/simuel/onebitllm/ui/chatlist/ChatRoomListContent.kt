package com.simuel.onebitllm.ui.chatlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme

@Composable
fun ChatRoomListContent(
    chats: List<ChatRoomItemUiState>,
    onItemClick: (ChatRoomItemUiState) -> Unit,
    onItemLongClick: (ChatRoomItemUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = chats,
            key = { it.id }
        ) { chat ->
            ChatListItem(
                chat = chat,
                onItemClick = onItemClick,
                onItemLongClick = onItemLongClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomListContentPreview() {
    OnebitLLMTheme {
        ChatRoomListContent(
            chats = listOf(
                ChatRoomItemUiState(1, "Title 1", "Hey, how are you?")
            ),
            onItemClick = {},
            onItemLongClick = {}
        )
    }
}
