package com.streamliners.timify.feature.pieChart

import androidx.compose.ui.graphics.toArgb
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.ui.theme.listOfColors
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_1
import com.streamliners.utils.DateTimeUtils.formatTime
import ir.mahozad.android.PieChart
import java.text.SimpleDateFormat
import java.util.Locale

class PieChartViewModel(
    private val taskInfoDao: TaskInfoDao
) : BaseViewModel() {

    private val currentDate = formatTime(DATE_MONTH_YEAR_1)
    val slices = taskStateOf<List<PieChart.Slice>>()

    fun start() {
        execute {
            onDateChanged()
        }
    }

    private fun calculateTimeDifference(start: String, end: String): Int {
        // Define the date format
        val format = SimpleDateFormat("hh a", Locale.getDefault())

        // Parse the start and end times
        val startDate = format.parse(start)
        val endDate = format.parse(end)

        // Calculate the difference in milliseconds
        val differenceInMillis = endDate.time - startDate.time

        // Convert milliseconds to hours
        return (differenceInMillis / (1000 * 60 * 60)).toInt()
    }

    private fun sliceFraction(start: String, end: String, totalHours: Int): Float {
        return calculateTimeDifference(start, end) * 1.0f / totalHours
    }

    private fun onDateChanged(){
        execute {
            val listOfTaskInfo = taskInfoDao.getList(currentDate).toMutableList()

            val totalHours = listOfTaskInfo.sumOf {
                calculateTimeDifference(it.startTime, it.endTime)
            }

            // TODO: Color needs to be different instead random
            slices.update(
                listOfTaskInfo.map { task ->
                    PieChart.Slice(
                        fraction = sliceFraction(task.startTime, task.endTime, totalHours),
                        color = listOfColors.random().toArgb(),
                        label = task.name
                    )
                }
            )
        }
    }

}