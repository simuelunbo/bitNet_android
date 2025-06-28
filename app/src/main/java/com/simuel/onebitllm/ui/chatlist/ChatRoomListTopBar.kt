package com.simuel.onebitllm.ui.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simuel.onebitllm.ui.theme.BackgroundColor
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme
import com.simuel.onebitllm.ui.theme.TitleColor

@Composable
fun ChatRoomListTopBar(
    onNewChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(48.dp))
        Text(
            text = "Chats",
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textAlign = TextAlign.Center,
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
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomListTopBarPreview() {
    OnebitLLMTheme {
        ChatRoomListTopBar(onNewChat = {})
    }
}
