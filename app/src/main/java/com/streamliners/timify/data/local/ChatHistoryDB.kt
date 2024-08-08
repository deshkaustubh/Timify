package com.streamliners.timify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.streamliners.timify.domain.ChatHistory


@Database(entities = [ChatHistory::class], version = 2)
abstract class ChatHistoryDB : RoomDatabase(){

    companion object {
        const val NAME = "chat_history_DB"
    }

    abstract fun chatHistoryDao(): ChatHistoryDao

}