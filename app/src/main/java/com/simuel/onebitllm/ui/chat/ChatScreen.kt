package com.simuel.onebitllm.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simuel.onebitllm.domain.model.ChatMessage
import com.simuel.onebitllm.ui.model.ChatState
import com.simuel.onebitllm.ui.theme.*


@Composable
fun ChatScreen(
    title: String,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ChatScreenContent(
        title = title,
        state = state,
        onBackClick = onBackClick,
        onSendMessage = { viewModel.sendMessage(it) }    )
}
@Composable
private fun ChatScreenContent(
    title: String,
    state: ChatState,
    onBackClick: () -> Unit,
    onSendMessage: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        ChatTopAppBar(title = title, onBackClick = onBackClick)

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items = state.messages, key = { it.id }) { message ->
                ChatMessageItem(message = message)
            }
        }

        var text by remember { mutableStateOf("") }
        ChatInput(
            value = text,
            onValueChange = { text = it },
            onSend = {
                onSendMessage(text)
                text = ""
            }
        )
    }
}

@Composable
fun ChatTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TitleColor
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TitleColor
        )
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isUser = message.isUser
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.Start else Arrangement.End
    ) {
        Column(horizontalAlignment = if (isUser) Alignment.Start else Alignment.End) {
            Text(
                text = if (isUser) "User" else "AI",
                style = MaterialTheme.typography.bodySmall,
                color = SubtitleColor,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) MaterialTheme.colorScheme.onPrimary else TitleColor,
                modifier = Modifier
                    .background(
                        color = if (isUser) UserBubbleColor else AiBubbleColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Message") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AiBubbleColor,
                unfocusedContainerColor = AiBubbleColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSend) { Text("Send") }
    }
}


@Preview(showBackground = true, name = "Top App Bar")
@Composable
private fun ChatTopAppBarPreview() {
    OnebitLLMTheme {
        ChatTopAppBar(title = "Ethan Harper", onBackClick = {})
    }
}

@Preview(showBackground = true, name = "User Message")
@Composable
private fun ChatMessageItemUserPreview() {
    OnebitLLMTheme {
        ChatMessageItem(
            ChatMessage(id = 1, chatId = 1, content = "Hello", isUser = true)
        )
    }
}

@Preview(showBackground = true, name = "AI Message")
@Composable
private fun ChatMessageItemAiPreview() {
    OnebitLLMTheme {
        ChatMessageItem(
            ChatMessage(id = 2, chatId = 1, content = "Hi there", isUser = false)
        )
    }
}

@Preview(showBackground = true, name = "Chat Input")
@Composable
private fun ChatInputPreview() {
    var text by remember { mutableStateOf("") }
    OnebitLLMTheme {
        ChatInput(value = text, onValueChange = { text = it }, onSend = {})
    }
}
@Preview(showBackground = true, name = "Chat Screen")
@Composable
private fun ChatScreenPreview() {
    OnebitLLMTheme {
        ChatScreenContent(
            title = "Chat Preview",
            state = ChatState(
                messages = sampleMessages
            ),
            onBackClick = {},
            onSendMessage = {}
        )
    }
}

private val sampleMessages: List<ChatMessage> = listOf(
    ChatMessage(id = 1, chatId = 1, content = "Hey there! How's your day going?", isUser = true),
    ChatMessage(id = 2, chatId = 1, content = "Hi Ethan! It's been pretty good, just finished a workout. How about you?", isUser = false),
    ChatMessage(id = 3, chatId = 1, content = "Nice! I've been working on a new project, it's quite challenging but exciting.", isUser = true),
    ChatMessage(id = 4, chatId = 1, content = "Sounds interesting! What kind of project is it?", isUser = false)
)
