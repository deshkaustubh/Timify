package com.streamliners.timify.feature.pieChart

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewModelScope
import com.streamliners.base.BaseViewModel
import com.streamliners.base.exception.log
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.timify.BuildConfig
import com.streamliners.timify.data.local.dao.TaskInfoDao
import com.streamliners.timify.ui.theme.listOfColors
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_1
import com.streamliners.utils.DateTimeUtils.formatTime
import ir.mahozad.android.PieChart
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PieChartViewModel(
    private val taskInfoDao: TaskInfoDao
) : BaseViewModel() {

    val currentDate = mutableStateOf(
        TextInputState("Date", value = formatTime(DATE_MONTH_YEAR_1))
    )
    val slices = taskStateOf<List<PieChart.Slice>>()

    fun start() {
        execute {
            onDateChanged()
        }
    }

    private fun calculateTimeDifferenceInMins(start: String, end: String): Int {
        // Define the date format
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Parse the start and end times
        val startDate = format.parse(start)
        val endDate = format.parse(end)

        // Calculate the difference in milliseconds
        val differenceInMillis = endDate.time - startDate.time

        // Convert milliseconds to minutes
        return (differenceInMillis / (1000 * 60)).toInt()
    }

    private fun sliceFraction(start: String, end: String, totalHours: Int): Float {
        return calculateTimeDifferenceInMins(start, end) * 1.0f / totalHours
    }

    fun onDateChanged(){
        viewModelScope.launch {
            val listOfTaskInfo = taskInfoDao.getList(currentDate.value()).toMutableList()

            val totalMins = listOfTaskInfo.sumOf {
                calculateTimeDifferenceInMins(it.startTime, it.endTime)
            }

            // TODO: Color needs to be different instead random

            val slicesList = listOfTaskInfo.map { task ->
                PieChart.Slice(
                    fraction = sliceFraction(task.startTime, task.endTime, totalMins),
                    color = listOfColors.random().toArgb(),
                    label = task.name
                )
            }

            log("slices : '$slicesList'", "pieChartDebug", buildType = BuildConfig.BUILD_TYPE)

            slices.update(slicesList)
        }
    }

}