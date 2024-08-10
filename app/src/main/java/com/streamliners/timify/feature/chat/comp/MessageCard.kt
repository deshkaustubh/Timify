package com.streamliners.timify.feature.chat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.streamliners.timify.feature.chat.ChatViewModel
import com.streamliners.timify.other.ext.message

@Composable
fun MessageCard(
    item: ChatViewModel.ChatHistoryUIItem,
) {
    Row {
        Card(
            modifier = Modifier.widthIn(max = 250.dp)
        ) {

            Row(
                modifier = Modifier.width(IntrinsicSize.Max)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = item.message().trim(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                        .padding(bottom = 8.dp)
                )

                Text(
                    modifier = Modifier.align(Alignment.Bottom),
                    text = item.formattedTime,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}