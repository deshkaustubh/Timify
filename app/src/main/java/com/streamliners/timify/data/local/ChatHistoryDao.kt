package com.streamliners.timify.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.timify.domain.ChatHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {

    @Query("SELECT * FROM CHATHISTORY")
    suspend fun getAllChats(): List<ChatHistory>

    @Insert
    suspend fun addChat(chatHistory: ChatHistory)

}