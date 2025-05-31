package com.simuel.onebitllm.ui.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun ChatRoomListEmptyState(
    onNewChat: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(BackgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "No conversations yet",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TitleColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Start a new conversation by tapping the button below.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TitleColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatRoomListEmptyStatePreview() {
    OnebitLLMTheme {
        ChatRoomListEmptyState(onNewChat = {})
    }
}
