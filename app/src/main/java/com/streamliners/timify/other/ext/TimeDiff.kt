package com.streamliners.timify.other.ext

import java.text.SimpleDateFormat
import java.util.Locale

fun calculateTimeDiffInMins(start: String, end: String): Int {
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