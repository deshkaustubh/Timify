package com.streamliners.timify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.streamliners.timify.domain.ChatHistory
import com.streamliners.timify.domain.PieChartInfo


@Database(entities = [ChatHistory::class, PieChartInfo::class], version = 4)
abstract class LocalDB : RoomDatabase(){

    companion object {
        const val NAME = "room_DB"
    }

    abstract fun chatHistoryDao(): ChatHistoryDao

    abstract fun pieChartInfoDao(): PieChartInfoDao
}