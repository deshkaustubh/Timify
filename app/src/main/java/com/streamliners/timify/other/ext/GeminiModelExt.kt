package com.streamliners.timify.other.ext

import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.asTextOrNull

suspend fun Chat.send(message: String): String {
    return sendMessage(message).text ?: error("Unable to get response from API")
}

fun Content.asString(): String {
    return parts.firstOrNull()?.asTextOrNull() ?: error("No text content found!")
}