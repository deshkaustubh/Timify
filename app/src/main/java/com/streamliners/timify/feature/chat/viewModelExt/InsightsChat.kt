package com.streamliners.timify.feature.chat.viewModelExt

import com.streamliners.timify.feature.chat.ChatViewModel

suspend fun ChatViewModel.insightResponseFor(modelResponse: String): String {
    return "Insight : $modelResponse"
}