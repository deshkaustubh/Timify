package com.streamliners.timify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.timify.domain.model.ChatHistoryItem

@Dao
interface ChatHistoryDao {

    @Query("SELECT * FROM ChatHistory WHERE date = :date")
    suspend fun getList(date: String): List<ChatHistoryItem>

    @Insert
    suspend fun add(chatHistoryItem: ChatHistoryItem)

}