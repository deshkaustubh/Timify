package com.streamliners.timify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.streamliners.utils.DateTimeUtils
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_1
import com.streamliners.utils.DateTimeUtils.formatTime

@Entity("ChatHistory")
data class ChatHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String = formatTime(DATE_MONTH_YEAR_1),
    val time: Long = System.currentTimeMillis(),
    val role: String,
    val type: String,
    val message: String
)