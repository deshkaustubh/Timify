package com.streamliners.timify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("TasksInfo")
data class TaskInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val startTime: String,
    val endTime: String,
    val name: String
)
