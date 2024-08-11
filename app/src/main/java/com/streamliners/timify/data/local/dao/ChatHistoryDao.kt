package com.streamliners.timify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.timify.domain.model.ChatHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {

    @Query("SELECT * FROM ChatHistory WHERE date = :date")
    fun getList(date: String): Flow<List<ChatHistoryItem>>

    @Insert
    suspend fun add(chatHistoryItem: ChatHistoryItem)

    @Query("SELECT COUNT(*) FROM ChatHistory")
    suspend fun getTotalRowCount(): Int

}