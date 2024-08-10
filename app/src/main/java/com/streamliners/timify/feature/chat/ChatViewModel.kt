package com.streamliners.timify.feature.chat

import com.streamliners.base.BaseViewModel

import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.streamliners.base.exception.defaultExecuteHandlingError
import com.streamliners.base.exception.log
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.data.local.dao.ChatHistoryDao
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.domain.model.ChatHistoryItem
import com.streamliners.timify.domain.model.TaskInfo
import com.streamliners.timify.feature.genAI.GeminiModel
import com.streamliners.timify.other.ext.send
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_1
import com.streamliners.utils.DateTimeUtils.formatTime

class ChatViewModel(
    private val chatHistoryDao: ChatHistoryDao,
    private val taskInfoDao: TaskInfoDao
) : BaseViewModel() {

    sealed class ChatListItem {
        class ModelMessage(
            val modelContent: Content
        ) : ChatListItem()

        class UserMessage(
            val userContent: Content
        ) : ChatListItem()
    }

    class Data(
        val chatListItems: List<ChatListItem>
    )

    private val generativeModel = GeminiModel.get()

    val data = taskStateOf<Data>()
    private val currentDate = formatTime(DATE_MONTH_YEAR_1)
    private lateinit var chat: Chat

    fun start() {
        execute {
            val chatHistory = chatHistoryDao.getList(currentDate).toContentList()
            chat = generativeModel.startChat(chatHistory)
            data.update(
                Data(chat.history.toChatListItems())
            )
        }
    }

    fun sendPrompt(prompt: String, onSuccess: () -> Unit) {
        execute {
            val response = chat.send(prompt)

            chatHistoryDao.add(
                ChatHistoryItem(
                    date = currentDate,
                    role = "user",
                    message = prompt
                )
            )

            chatHistoryDao.add(
                ChatHistoryItem(
                    date = currentDate,
                    role = "model",
                    message = response
                )
            )

            data.update(
                Data(chat.history.toChatListItems())
            )

            onSuccess()
        }
    }

    fun saveTaskInfoToLocal(onSuccess: () -> Unit) {
        execute {
            val response = chat.send("give data in csv")

            val lines = response.split("\n").dropLast(1)

            log("response from model : '$response'", "pieChartDebug", buildType = BuildConfig.BUILD_TYPE)

            taskInfoDao.clear()

            lines.forEach { line ->
                val data = line.split(",")

                taskInfoDao.add(
                    TaskInfo(
                        name = data[0],
                        startTime = data[1],
                        endTime = data[2],
                        date = currentDate
                    )
                )
            }

            executeOnMain { onSuccess() }
        }
    }

    private fun List<ChatHistoryItem>.toContentList(): MutableList<Content> {
        return map { item ->
            content(role = item.role) {
                text(item.message)
            }
        }.toMutableList()
    }

    private fun List<Content>.toChatListItems(): List<ChatListItem> {
        return map { content ->
            if (content.role == "user") {
                ChatListItem.UserMessage(content)
            } else {
                ChatListItem.ModelMessage(content)
            }
        }
    }
}