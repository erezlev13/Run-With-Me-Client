package com.runwithme.runwithme.utils

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TimeUtils {

    fun calculateTimeDifference(startTime:String, endTime:String): String{
        val start = stringToLocalTime(startTime)
        val end = stringToLocalTime(endTime)
        val duration: Duration = Duration.between(start, end)

        return convertSecondsToHours(duration.seconds)
    }

    fun stringToLocalDate(stringDate : String) : LocalDate{
        val splitedString = stringDate.split("-","T")
        return LocalDate.of(splitedString[0].toInt(),splitedString[1].toInt(),splitedString[2].toInt())
    }

    private fun stringToLocalTime(stringTime : String) : LocalTime{

        val splitedString = stringTime.split("T",":",".")
        return LocalTime.of(splitedString[1].toInt(),splitedString[2].toInt(),splitedString[3].toInt())
    }

    private fun convertSecondsToHours(seconds:Long): String {
        val numberOfHours : Int = (seconds / 3600).toInt()
        val numberOfMinutes : Int = (seconds / 60).toInt()
        val numberOfSeconds : Int = (seconds % 60).toInt()
        val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        return LocalTime.of(numberOfHours, numberOfMinutes, numberOfSeconds).format(dtf)

    }
}