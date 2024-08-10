package com.streamliners.timify.feature.pieChart

import android.graphics.Color
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.timify.TimifyApp
import com.streamliners.timify.domain.PieChartInfo
import ir.mahozad.android.PieChart
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PieChartViewModel : BaseViewModel() {

    val pieChartInfoDao = TimifyApp.localDB.pieChartInfoDao()

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val currentDate = LocalDateTime.now().format(formatter)

    // TODO: Why it is in var
    var listOfPieChartInfo = mutableListOf<PieChartInfo>()
    val slice = taskStateOf<List<PieChart.Slice>>()

    fun getPieChartData() {

        execute {
            listOfPieChartInfo = pieChartInfoDao.getPieChartInfo(currentDate).toMutableList()
            setDate()
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

    private fun fraction(start: String, end: String, totalHours: Int):Float{

        val fraction: Float = calculateTimeDifference(start, end) * 1.0f / totalHours

        return fraction
    }


    // TODO: Where should I call it?
    fun setDate(){
        execute {
            val list = mutableListOf<PieChart.Slice>()
            val totalHours = mutableListOf<Int>()
            listOfPieChartInfo.forEach {
                totalHours.add(
                    calculateTimeDifference(it.startTime, it.endTime)
                )
            }
            listOfPieChartInfo.forEach {
                    list.add(
                        PieChart.Slice(fraction = fraction(it.startTime, it.endTime, totalHours.sum()) ,color = Color.BLUE, label = it.taskName)
                    )
            }
            slice.update(
                list
            )
        }
    }

}