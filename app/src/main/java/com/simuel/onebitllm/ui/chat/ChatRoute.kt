package com.simuel.onebitllm.ui.chat

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatRoute(
    chatId: Long,
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    ChatScreen(
        title = "Chat $chatId",
        onBackClick = onBack,
        viewModel = viewModel
    )
}
