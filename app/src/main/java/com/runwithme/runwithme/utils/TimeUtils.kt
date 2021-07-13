package com.runwithme.runwithme.utils

import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TimeUtils {

    fun calculateTimeDifference(startTime:LocalTime, endTime:LocalTime): String{
        val duration: Duration = Duration.between(startTime, endTime)

        return convertSecondsToHours(duration.seconds)
    }

    private fun convertSecondsToHours(seconds:Long): String {
        val numberOfHours : Int = (seconds / 3600).toInt()
        val numberOfMinutes : Int = (seconds / 60).toInt()
        val numberOfSeconds : Int = (seconds % 60).toInt()
        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        return LocalTime.of(numberOfHours, numberOfMinutes, numberOfSeconds).format(dtf)

    }
}