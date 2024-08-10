package com.streamliners.timify.feature.chat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.type.asTextOrNull
import com.streamliners.timify.feature.chat.ChatViewModel
import com.streamliners.timify.feature.chat.ChatViewModel.ChatListItem

@Composable
fun MessagesList(
    data: ChatViewModel.Data
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data.chatListItems) { contentListItem ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = when (contentListItem) {
                    is ChatListItem.UserMessage -> Alignment.CenterEnd
                    is ChatListItem.ModelMessage -> Alignment.CenterStart
                }
            ) {
                when (contentListItem) {

                    is ChatListItem.ModelMessage -> {
                        MessageCard(
                            message = contentListItem.modelContent.parts.first().asTextOrNull().toString(),

                        )
                    }
                    is ChatListItem.UserMessage -> {
                        MessageCard(message = contentListItem.userContent.parts.first().asTextOrNull().toString())
                    }
                }
            }
        }
    }
}