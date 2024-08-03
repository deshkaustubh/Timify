package com.streamliners.timify.chat

import com.streamliners.base.BaseViewModel

import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.timify.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : BaseViewModel() {


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

    val chatHistoryState = mutableListOf<Content>()

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
                "You are a time managing assistant, helping user in knowing where they spend most of their time. Also, providing analytics as to what percentage of time is spent on what tasks day / week / month wise. For this you have to converse with the user with sole aim of finding where and how they spent their day. With task, we aim to collect only the name of the task like - Writing blog, working on Foo project, going FooBar place, walking, etc. Ask questions like - “What were you working on recently?”, “With what task did you start your day with?”. When user. says Hi, start asking such questions and collect the data as to how they spent their day. After conversing, when prompted - “Give data”, you must provide the summary as to how user spent the day in this format :\n\n10 AM - 12 PM : A Project\n\n12 PM - 2 PM : Lunch and Rest\n\n2 PM - 4 PM : Code Review\n\n4 PM - 5 PM : Gym\n\n5 PM - 6 PM : Dinner\n\n6 PM - 7 PM : Walk\n\n7 PM - 9 PM : Meeting\n\nMake sure to keep collecting entire day’s data by asking relevant questions. Output must be of continuous time slots with what was done in each."
            )
        },
    )

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = UiState.Loading


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.startChat(chatHistoryState).sendMessage(prompt)

                chatHistoryState.add(
                    content("user") {
                        text(prompt)
                    }
                )

                response.text?.let { outputContent ->
                    chatHistoryState.add(
                        content("model") {
                            text(outputContent)
                        }
                    )

                    data.update(
                        Data(
                            contentListItems = createContentListItem(chatHistoryState)
                        )
                    )

                    _uiState.value = UiState.Success(outputContent)
                }

            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
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