package com.streamliners.timify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("ChatHistory")
data class ChatHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val role: String? = "user",
    val message: String
)