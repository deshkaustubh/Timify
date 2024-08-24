package com.streamliners.timify.other.ext

import com.streamliners.timify.domain.model.CustomAttribute
import com.streamliners.timify.domain.model.TaskInfo

fun List<TaskInfo>.toRows(): List<List<String>> {
    return buildList {
        add(
            listOf("Date", "Start", "End", "Task Name")
        )

        var currentDate = ""

        this@toRows.forEach {
            with(it) {
                if (it.date != currentDate) {
                    val isFirst = currentDate.isBlank()
                    currentDate = it.date
                    if (!isFirst) add(listOf("-", "-", "-", "-"))
                }

                add(listOf(date, startTime, endTime, name))
            }
        }
    }
}

fun List<List<String>>.parseAsTaskInfoList(): List<TaskInfo> {
    return drop(1).map { fields ->
        TaskInfo(
            date = fields[0],
            startTime = fields[1],
            endTime = fields[2],
            name = fields[3],
            durationInMins = calculateTimeDiffInMins(fields[1], fields[2])
        )
    }
}
fun parseAsCustomAttributeList(rows: List<List<String>>, lastId: Int): List<CustomAttribute> {

    val listOfKeyName = mutableListOf<String>()
    rows[0].forEachIndexed { index, string ->
            if (index > 3) {
                listOfKeyName.add(string)
            }
    }

    val listOfCustomAttribute = mutableListOf<CustomAttribute>()

     rows.drop(1).mapIndexed { index, fields ->
        for (i in 0 until listOfKeyName.size){

            listOfCustomAttribute.add(
                CustomAttribute(
                    taskId = index + lastId,
                    key = listOfKeyName[i],
                    value = fields[ 4 + i]
                )
            )
        }

    }
    return listOfCustomAttribute
}