package com.streamliners.timify.chat.comp

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
import com.streamliners.timify.chat.ChatViewModel
import com.streamliners.timify.chat.ChatViewModel.ContentListItem

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
        items(data.contentListItems) { contentListItem ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = when (contentListItem) {
                    is ContentListItem.UserMessage -> Alignment.CenterEnd
                    is ContentListItem.ModelMessage -> Alignment.CenterStart
                }
            ) {
                when (contentListItem) {

                    is ContentListItem.ModelMessage -> {
                        MessageCard(
                            message = contentListItem.modelContent.parts.first().asTextOrNull().toString(),

                        )
                    }
                    is ContentListItem.UserMessage -> {
                        MessageCard(message = contentListItem.userContent.parts.first().asTextOrNull().toString())
                    }
                }

            }

        }
    }

}