package com.streamliners.timify.feature.chat.viewModelExt

import androidx.sqlite.db.SimpleSQLiteQuery
import com.streamliners.timify.feature.chat.ChatViewModel

suspend fun ChatViewModel.insightResponseFor(modelResponse: String): String {
    val parts = modelResponse.replace("```sql\n", "")
        .replace("```\n", "")
        .replace("\n", " ")
        .trim()
        .split(";")
        .map { it.trim() }

    val query = parts[0]
    val outputType = parts[1].split(" : ")[1]

    return taskInfoDao.rawQueryAsInt(SimpleSQLiteQuery(query)).toString()
}