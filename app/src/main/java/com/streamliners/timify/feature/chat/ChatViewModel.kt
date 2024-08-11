package com.streamliners.timify.feature.chat

import androidx.compose.runtime.mutableStateOf
import com.streamliners.base.BaseViewModel

import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.streamliners.base.exception.log
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.ext.hideLoader
import com.streamliners.base.ext.showLoader
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.android.helper.TTSHelper
import com.streamliners.timify.data.local.dao.ChatHistoryDao
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.domain.model.ChatHistoryItem
import com.streamliners.timify.domain.model.TaskInfo
import com.streamliners.timify.feature.chat.ChatViewModel.ChatHistoryUIItem.Role.Model
import com.streamliners.timify.feature.chat.ChatViewModel.ChatHistoryUIItem.Role.User
import com.streamliners.timify.feature.genAI.GeminiModel
import com.streamliners.timify.other.ext.send
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_1
import com.streamliners.utils.DateTimeUtils.Format.Companion.HOUR_MIN_12
import com.streamliners.utils.DateTimeUtils.formatTime
import kotlinx.coroutines.flow.collectLatest

class ChatViewModel(
    private val chatHistoryDao: ChatHistoryDao,
    private val taskInfoDao: TaskInfoDao,
    val ttsHelper: TTSHelper
) : BaseViewModel() {

    data class ChatHistoryUIItem(
        val content: Content,
        val time: Long,
        val formattedTime: String,
        val date: String,
        val role: Role
    ) {
        enum class Role { User, Model }
    }

    class Data(
        val chatHistoryUIItems: List<ChatHistoryUIItem>
    )

    enum class Mode { Text, Voice }

    private val generativeModel = GeminiModel.get()

    val mode = mutableStateOf(Mode.Text)
    val data = taskStateOf<Data>()
    private val currentDate = formatTime(DATE_MONTH_YEAR_1)
    private lateinit var chat: Chat

    var isNewChatHappened = mutableStateOf(true)

    fun start() {
        execute(false) {
            chatHistoryDao.getList(currentDate).collectLatest { chatHistory ->
                if (!this@ChatViewModel::chat.isInitialized) {
                    chat = generativeModel.startChat(chatHistory.toContentList())
                }
                data.update(
                    Data(chatHistory.toUIItems())
                )
            }
        }
    }

    fun sendPrompt(
        prompt: String,
        onSuccess: () -> Unit,
        takeNextVoicePrompt: () -> Unit
    ) {
        execute(false) {
            showLoader()
            isNewChatHappened.value = true
            val response = chat.send(prompt)

            chatHistoryDao.add(
                ChatHistoryItem(
                    role = "user",
                    message = prompt
                )
            )

            chatHistoryDao.add(
                ChatHistoryItem(
                    role = "model",
                    message = response
                )
            )

            hideLoader()
            onSuccess()

            if (mode.value == Mode.Voice) {
                ttsHelper.speak(response)
                takeNextVoicePrompt()
            }
        }
    }

    fun saveTaskInfoToLocal(onSuccess: () -> Unit) {
            execute {
                if (isNewChatHappened.value) {
                    val rowsCount = chatHistoryDao.getTotalRowCount()
                    if (rowsCount == 0) {
                        executeOnMain { onSuccess() }
                        return@execute
                    }

                    val response = chat.send("give data in csv")

                    val lines = response.split("\n").dropLast(1)

                    log(
                        "response from model : '$response'",
                        "pieChartDebug",
                        buildType = BuildConfig.BUILD_TYPE
                    )

                    taskInfoDao.clearAllOf(currentDate)

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
                }
                executeOnMain { onSuccess() }
            }
    }

    private fun List<ChatHistoryItem>.toContentList(): MutableList<Content> {
        return map { it.extractContent() }.toMutableList()
    }

    private fun List<ChatHistoryItem>.toUIItems(): List<ChatHistoryUIItem> {
        return map { item ->
            ChatHistoryUIItem(
                content = item.extractContent(),
                time = item.time,
                formattedTime = formatTime(HOUR_MIN_12, item.time),
                date = formatTime(DATE_MONTH_YEAR_1, item.time),
                role = if (item.role == "user") User else Model
            )
        }
    }

    private fun ChatHistoryItem.extractContent(): Content {
        return content(role = role) { text(message) }
    }
}