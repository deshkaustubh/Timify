package com.streamliners.timify.other.ext

import com.google.ai.client.generativeai.Chat

suspend fun Chat.send(message: String): String {
    return sendMessage(message).text ?: error("Unable to get response from API")
}