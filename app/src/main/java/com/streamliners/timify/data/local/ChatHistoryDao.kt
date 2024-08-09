package com.streamliners.timify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.timify.domain.ChatHistory

@Dao
interface ChatHistoryDao {

    @Query("SELECT * FROM CHATHISTORY WHERE DATE = :date")
    suspend fun getAllChats(date: String): List<ChatHistory>

    @Insert
    suspend fun addChat(chatHistory: ChatHistory)

}