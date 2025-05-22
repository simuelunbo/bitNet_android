package com.simuel.onebitllm.ui.chatlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.simuel.onebitllm.ui.theme.OnebitLLMTheme

@Composable
fun ChatListScreen(modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(
            animationSpec = tween(),
            initialOffsetX = { fullWidth -> fullWidth }
        ),
        exit = ExitTransition.None
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Chat List Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatListScreenPreview() {
    OnebitLLMTheme {
        ChatListScreen()
    }
}