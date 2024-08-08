package com.streamliners.timify.domain

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ChatHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: Long,

    val role: String? = "user",

    val message: String
)
