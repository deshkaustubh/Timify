package com.streamliners.timify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.streamliners.timify.domain.model.CustomAttribute

@Dao
interface CustomAttributeDao {


    @Query("SELECT * FROM CustomAttribute")
    suspend fun getAll(): List<CustomAttribute>

    @Query("DELETE FROM CustomAttribute")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(customAttribute: CustomAttribute)

    @Insert
    suspend fun addAll(customAttribute: List<CustomAttribute>)

    @Query("SELECT DISTINCT `key` FROM CustomAttribute")
    fun getDistinctKeys(): List<String>

    @Query("SELECT * FROM CustomAttribute WHERE taskId = :taskId")
    fun getCustomAttributesByTaskId(taskId: Int): List<CustomAttribute>

}