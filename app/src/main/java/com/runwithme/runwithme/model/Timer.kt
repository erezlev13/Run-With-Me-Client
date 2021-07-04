package com.runwithme.runwithme.model

import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * This [Timer] class made for count time, and by that update the UI
 * with the current time. The time is initialized to 00:00.00, and
 * counting seconds.
 * */

class Timer {

    /** Properties: */
    private var startTime: Long = 0
    private var customHandler: Handler = Handler()
    var timeInMilliseconds: Long = 0
    lateinit var timeTextView: TextView

    /** Methods: */
    fun start() {
        startTime = SystemClock.uptimeMillis()
        customHandler.postDelayed(updateTimerThread, 0)
    }

    fun continueTime() {
        startTime = SystemClock.uptimeMillis() - timeInMilliseconds
        customHandler.postDelayed(updateTimerThread, 0)
    }

    fun stop() {
        customHandler.removeCallbacks(updateTimerThread)
    }

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            timeTextView.text = getDateFromMillis(timeInMilliseconds)
            customHandler.postDelayed(this, 1000)
        }
    }

    private fun getDateFromMillis(d: Long): String? {
        val timeInMinutes = (d * (1000 * 60)) % 60
        val df = if (timeInMinutes > 60) {
            SimpleDateFormat("HH:mm:ss", Locale.US)
        } else {
            SimpleDateFormat("mm:ss", Locale.US)
        }

        df.timeZone = TimeZone.getTimeZone("GMT")
        return df.format(d)
    }
}