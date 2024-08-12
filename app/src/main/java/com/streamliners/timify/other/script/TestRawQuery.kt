package com.streamliners.timify.other.script

import androidx.sqlite.db.SimpleSQLiteQuery
import com.streamliners.base.ext.showMessageDialog
import com.streamliners.timify.ui.main.MainActivity

fun MainActivity.testRawQuery() {
    execute {
        val x = taskInfoDao.rawQueryAsIntList(
            SimpleSQLiteQuery(
                """
                        SELECT
                          SUM(CASE WHEN date >= '2024/08/05' AND date <= '2024/08/11' THEN durationInMins ELSE 0 END) AS week4,
                          SUM(CASE WHEN date >= '2024/07/29' AND date <= '2024/08/04' THEN durationInMins ELSE 0 END) AS week3,
                          SUM(CASE WHEN date >= '2024/07/22' AND date <= '2024/07/28' THEN durationInMins ELSE 0 END) AS week2,
                          SUM(CASE WHEN date >= '2024/07/15' AND date <= '2024/07/21' THEN durationInMins ELSE 0 END) AS week1
                        FROM TasksInfo
                        WHERE name = 'Alpha Project';
                    """.trimIndent()
            )
        )

        showMessageDialog("Result", "$x")
    }
}