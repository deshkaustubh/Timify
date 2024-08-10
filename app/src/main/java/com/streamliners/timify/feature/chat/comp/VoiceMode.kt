package com.streamliners.timify.feature.chat.comp

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.streamliners.timify.feature.chat.ChatViewModel

@Composable
fun VoiceMode(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
) {
    Button(
        modifier = modifier,
        onClick = {
            viewModel.ttsHelper.stop()
            viewModel.mode.value = ChatViewModel.Mode.Text
        }
    ) {
        Text(text = "Voice Mode On! Tap to stop.")
    }
}