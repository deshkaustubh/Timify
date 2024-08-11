package com.streamliners.timify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.streamliners.timify.domain.model.TaskInfo

@Dao
interface TaskInfoDao {

    @Query("SELECT * FROM TasksInfo WHERE date = :date")
    suspend fun getList(date: String): List<TaskInfo>

    @Query("SELECT * FROM TasksInfo")
    suspend fun getAll(): List<TaskInfo>

    @Query("DELETE FROM TasksInfo")
    suspend fun clear()

    @Query("DELETE FROM TasksInfo WHERE date = :date")
    suspend fun clearAllOf(date: String)

    @Insert
    suspend fun add(taskInfo: TaskInfo)

    @Insert
    suspend fun addAll(taskInfo: List<TaskInfo>)

    @Query("SELECT COUNT(*) FROM TasksInfo")
    suspend fun getTotalRowCount(): Int

}