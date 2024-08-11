package com.streamliners.timify.feature.chat.viewModelExt

import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.streamliners.timify.domain.model.ChatHistoryItem
import com.streamliners.timify.feature.chat.ChatViewModel
import com.streamliners.utils.DateTimeUtils

fun List<ChatHistoryItem>.toContentList(): MutableList<Content> {
    return map { it.extractContent() }.toMutableList()
}

fun List<ChatHistoryItem>.toUIItems(): List<ChatViewModel.ChatHistoryUIItem> {
    return map { item ->
        ChatViewModel.ChatHistoryUIItem(
            content = item.extractContent(),
            time = item.time,
            formattedTime = DateTimeUtils.formatTime(DateTimeUtils.Format.HOUR_MIN_12, item.time),
            date = DateTimeUtils.formatTime(DateTimeUtils.Format.DATE_MONTH_YEAR_1, item.time),
            role = if (item.role == "user") ChatViewModel.ChatHistoryUIItem.Role.User else ChatViewModel.ChatHistoryUIItem.Role.Model
        )
    }
}

fun ChatHistoryItem.extractContent(): Content {
    return content(role = role) { text(message) }
}