package com.streamliners.timify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CustomAttribute")
data class CustomAttribute(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val key: String,
    val value: String
)
