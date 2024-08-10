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
import com.streamliners.timify.feature.chat.ChatViewModel
import com.streamliners.timify.feature.chat.ChatViewModel.ChatHistoryUIItem.Role.Model
import com.streamliners.timify.feature.chat.ChatViewModel.ChatHistoryUIItem.Role.User

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
        items(data.chatHistoryUIItems) { item ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = when (item.role) {
                    User -> Alignment.CenterEnd
                    Model -> Alignment.CenterStart
                }
            ) {
                when (item.role) {
                    User -> {
                        MessageCard(item)
                    }
                    Model -> {
                        MessageCard(item)
                    }
                }
            }
        }
    }
}