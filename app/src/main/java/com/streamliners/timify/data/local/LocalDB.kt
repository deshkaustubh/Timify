package com.streamliners.timify.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.streamliners.timify.data.local.dao.ChatHistoryDao
import com.streamliners.timify.data.local.dao.CustomAttributeDao
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.domain.model.ChatHistoryItem
import com.streamliners.timify.domain.model.CustomAttribute
import com.streamliners.timify.domain.model.TaskInfo

@Database(entities = [ChatHistoryItem::class, TaskInfo::class, CustomAttribute::class], version = 1)
abstract class LocalDB : RoomDatabase(){

    companion object {
        fun create(context: Context): LocalDB {
            return Room.databaseBuilder(
                context = context,
                klass = LocalDB::class.java,
                name = "roomDB"
            ).build()
        }
    }

    abstract fun chatHistoryDao(): ChatHistoryDao

    abstract fun taskInfoDao(): TaskInfoDao

    abstract fun customAttributeDao(): CustomAttributeDao
}