package com.streamliners.timify.feature.chat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamliners.timify.feature.chat.ChatViewModel

@Composable
fun TextInput(
    prompt: MutableState<String>,
    viewModel: ChatViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            value = prompt.value,
            label = { Text("Type something") },
            onValueChange = { prompt.value = it }
        )

        FilledIconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            enabled = prompt.value.isNotEmpty(),
            onClick = {
                viewModel.sendPrompt(
                    prompt.value,
                    onSuccess = { prompt.value = "" },
                    takeNextVoicePrompt = {}
                )
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Voice Input"
            )
        }
    }
}