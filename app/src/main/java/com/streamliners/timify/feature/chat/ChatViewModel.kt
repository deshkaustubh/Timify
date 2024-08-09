package com.streamliners.timify.feature.chat

import com.streamliners.base.BaseViewModel

import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.TimifyApp
import com.streamliners.timify.domain.ChatHistory

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ChatViewModel : BaseViewModel() {

    val chatHistoryDao = TimifyApp.localDB.chatHistoryDao()
    //val pieChartInfoDao = TimifyApp.localDB.pieChartInfoDao()

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val currentDate = LocalDateTime.now().format(formatter)

    sealed class ContentListItem {
        class ModelMessage(
            val modelContent: Content
        ) : ContentListItem()


        class UserMessage(
            val userContent: Content
        ) : ContentListItem()
    }

    class Data(
        val contentListItems: List<ContentListItem>
    )

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    var chatHistoryState = mutableListOf<Content>()

    val data = taskStateOf<Data>()

    private val generativeModel = GenerativeModel(
        "gemini-1.5-flash",
        BuildConfig.apiKey,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 64
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
        systemInstruction = content {
            text(
                Constant.SYSTEM_INSTRUCTION
            )
        },
    )

    lateinit var chat:Chat

    fun start(){
        execute {
            chatHistoryState = getPreviousChatFromRoomDb()
            chat = generativeModel.startChat(chatHistoryState)
            data.update(
                Data(contentListItems = createContentListItem(chat.history)
                )
            )
        }
    }


   /* private fun getPieChartInfoList(prompt: String): List<PieChartInfo> {



    }*/

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = chat.sendMessage(prompt)

                    chatHistoryDao.addChat(
                        ChatHistory(
                            date = currentDate,
                            role = "user",
                            message = prompt
                        )
                    )


                response.text?.let { outputContent ->

                    chatHistoryDao.addChat(
                        ChatHistory(
                            date = currentDate,
                            role = "model",
                            message = outputContent
                        )
                    )

                    data.update(
                        Data(contentListItems = createContentListItem(chat.history)
                        )
                    )

                    _uiState.value = UiState.Success(outputContent)
                }

            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    private suspend fun getPreviousChatFromRoomDb(): MutableList<Content> {

        val contentList = mutableListOf<Content>()
        val chatFromRoomDb = chatHistoryDao.getAllChats(currentDate)
        chatFromRoomDb.forEach {

            contentList.add(
                content(role = it.role) {
                    text(it.message)
                }
            )

        }

        return contentList
    }


    private fun createContentListItem(chatHistoryState: MutableList<Content>): List<ContentListItem> {

        val contentListItem = mutableListOf<ContentListItem>()

        chatHistoryState.forEach {
            if (it.role == "user") {
                contentListItem.add(
                    ContentListItem.UserMessage(
                        userContent = it
                    )
                )
            } else {
                contentListItem.add(
                    ContentListItem.ModelMessage(
                        modelContent = it
                    )
                )

            }

        }
        return contentListItem
    }
}



