package com.simuel.onebitllm.data.db.entities

data class ChatWithLastMessage(
    val id: Long,
    val title: String,
    val lastMessage: String?
)
