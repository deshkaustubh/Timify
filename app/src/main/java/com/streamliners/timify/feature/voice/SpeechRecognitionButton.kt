package com.streamliners.timify.feature.voice

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Locale

@Composable
fun SpeechRecognitionButton(
    modifier: Modifier = Modifier,
    onInput: (input: String, nextInput: () -> Unit) -> Unit,
    showError: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var requestId by remember { mutableStateOf(0) }

    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(EXTRA_RESULTS)
            data?.get(0)?.let { onInput(it) { requestId++ } }
                ?: showError("No speech detected.")
        } else {
            onDismiss()
        }
    }

    val launch = {
        launcher.launch(
            Intent(ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM)
                putExtra(EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(EXTRA_PROMPT, "Go on then, say something.")
            }
        )
    }

    LaunchedEffect(key1 = requestId) {
        if (requestId > 0) launch()
    }

    FilledTonalIconButton(
        modifier = modifier,
        onClick = launch
    ) {
        Icon(imageVector = Icons.Default.KeyboardVoice, contentDescription = "Voice Input")
    }
}