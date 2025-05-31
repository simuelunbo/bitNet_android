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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simuel.onebitllm.ui.model.ChatRoomItemUiState
import com.simuel.onebitllm.ui.theme.BackgroundColor
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme
import com.simuel.onebitllm.ui.theme.TitleColor

@Composable
fun ChatRoomListScreen(
    chats: List<ChatRoomItemUiState>, modifier: Modifier = Modifier
) {
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
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Chat",
                    tint = TitleColor
                )
            }
        }

        if (chats.isEmpty()) {
            ChatRoomListEmptyState(
                onNewChat = {}, modifier = Modifier.weight(1f)
            )
        } else {
            ChatRoomListContent(
                chats = chats, modifier = Modifier.weight(1f)
            )
        }


        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatListScreenPreview() {
    OnebitLLMTheme {
        ChatRoomListScreen(sampleChats())
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyChatListScreenPreview() {
    OnebitLLMTheme {
        ChatRoomListScreen(emptyList())
    }
}

private fun sampleChats(): List<ChatRoomItemUiState> = listOf(
    ChatRoomItemUiState(1, "Title 1", "Hey, how are you?"),
    ChatRoomItemUiState(2, "Title 2", "I'm on my way"),
    ChatRoomItemUiState(3, "Title 3", "See you soon"),
    ChatRoomItemUiState(4, "Title 4", "I'm here"),
)
