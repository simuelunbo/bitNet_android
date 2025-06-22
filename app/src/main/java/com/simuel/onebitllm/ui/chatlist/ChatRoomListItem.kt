package com.simuel.onebitllm.ui.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.simuel.onebitllm.ui.theme.SubtitleColor
import com.simuel.onebitllm.ui.theme.TitleColor

@Composable
fun ChatListItem(
    chat: ChatRoomItemUiState,
    onItemClick: (ChatRoomItemUiState) -> Unit,
    onItemLongClick: (ChatRoomItemUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundColor)
            .heightIn(min = 72.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = { onItemClick(chat) },
                onLongClick = { onItemLongClick(chat) }
            )
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = chat.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = TitleColor,
            )
            Text(
                text = chat.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = SubtitleColor,
            )
        }
    }
}

@Preview
@Composable
private fun ChatListItemPreview() {
    ChatListItem(
        chat = ChatRoomItemUiState(
            id = 1,
            name = "Chat 1",
            lastMessage = "Last message"
        ),
        onItemClick = {},
        onItemLongClick = {}
    )
}
