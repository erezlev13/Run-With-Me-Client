package com.runwithme.runwithme.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.databinding.ActivityOnRunningBinding
import com.runwithme.runwithme.model.Timer
import com.runwithme.runwithme.service.TrackerService
import com.runwithme.runwithme.utils.Constants.ACTION_SERVICE_START
import com.runwithme.runwithme.utils.Constants.ACTION_SERVICE_STOP
import com.runwithme.runwithme.utils.Constants.AVG_PACE
import com.runwithme.runwithme.utils.Constants.DISTANCE
import com.runwithme.runwithme.utils.Constants.LOCATIONS
import com.runwithme.runwithme.utils.Constants.TIME
import com.runwithme.runwithme.utils.MapUtils
import com.runwithme.runwithme.view.activity.summary.SummaryActivity
import com.runwithme.runwithme.view.run.bottomsheet.RunBottomSheet

/** Constants: */
private const val TAG = "OnRunningActivity"
private const val KM = 1000.0

class OnRunningActivity : AppCompatActivity(), RunBottomSheet.OnContinueStopClick {

    /** Properties: */
    private lateinit var binding: ActivityOnRunningBinding

    private var locationList = mutableListOf<LatLng>()
    private val timer = Timer()

    private var totalDistance = 0.0
    private var sumPace = 0.0
    private var paceCounter = 0
    private var currentPace = 0.0
    private var avgPace = 0.0
    private var lastPace = 0.0
    private var isContinued = false

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        binding = ActivityOnRunningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startRun()
        observeTrackerService()
        onPauseClickListener()
        // TODO: calculate steps!!
    }

    override fun onBackPressed() {
        Snackbar.make(binding.root, "Press pause to quit running :)", Snackbar.LENGTH_LONG).show()
    }

    /** Class Methods: */
    private fun startRun() {
        sendActionCommandToService(ACTION_SERVICE_START)
    }

    private fun observeTrackerService() {
        // Set timer, so the time will appear on the screen.
        TrackerService.isStarted.observe(this, { isStarted ->
            if (isStarted && !isContinued) {
                Log.d(TAG, "started")
                timer.start()
                startTime()
            } else if (isContinued) {
                Log.d(TAG, "continued")
                timer.continueTime()
                isContinued = false
            }
        })

        // Get coordinates and calculates distance.
        TrackerService.locationList.observe(this, {
            locationList = it
            totalDistance += MapUtils.calculateTheDistance(it)
            if (binding.distanceTextView.isVisible) {
                binding.distanceTextView.text = MapUtils.getDistance(totalDistance)
                showMeasurements()  // Show current pace, duration and average pace, on every single location received from GPS.
            }
        })
    }

    private fun startTime() {
        showMeasurements()
    }

    private fun showMeasurements() {
        binding.currentPaceTextView.text = calculateCurrentPace(totalDistance, timer.timeInMilliseconds)
        binding.averagePaceTextView.text = calculateAvgPace()
        timer.timeTextView = binding.durationTextView
    }

    private fun calculateCurrentPace(totalDistance: Double, timeInMS: Long): String {
        // Get the current pace in MS.
        if (totalDistance != 0.0) {
            currentPace = timeInMS / totalDistance
        }

        // Count current pace every 1KM. So, we can get an average of every 1KM of running.
        if (totalDistance == 1 * KM) {
            sumAndCountPace()
        } else if (totalDistance > 1 * KM && isPassedAKilometer()) {
            sumAndCountPace()
        }

        // Set minutes and seconds to show current pace.
        return getTimeInMinutesAndSeconds(currentPace)
    }

    private fun sumAndCountPace() {
        // Update sum and counter, to get average of pace.
        sumPace += currentPace
        paceCounter++
    }

    private fun calculateAvgPace(): String {
        if (totalDistance < 1 * KM) {
            // The user is on the first km. So, the average pace is the current pace.
            avgPace = currentPace
        } else if (isPassedAKilometer()) {
            // The user pass 1KM. Therefore, we'll calculate average based on each 1KM the user passed.
            if (paceCounter != 0) {
                avgPace = sumPace / paceCounter
            }
        }

        // Set minutes and seconds to show average pace.
        return getTimeInMinutesAndSeconds(avgPace)
    }

    private fun isPassedAKilometer(): Boolean {
        return (totalDistance % KM).toInt() == paceCounter + 1
    }

    private fun getTimeInMinutesAndSeconds(time: Double): String {
        val minutes = (time / (1000 * 60) % 60)
        val seconds = (time / 1000).toInt() % 60

        val minutesStr = if (minutes < 10) "0${minutes.toInt()}" else "${minutes.toInt()}"
        val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"

        return "$minutesStr:$secondsStr"
    }

    private fun onPauseClickListener() {
        binding.pauseButton.setOnClickListener {
            lastPace = currentPace
            sendActionCommandToService(ACTION_SERVICE_STOP)
            timer.stop()
            showBottomSheet()
        }
    }

    private fun sendActionCommandToService(action: String) {
        Log.d(TAG, "sendActionCommandToService: called")
        Intent(
            this as Context,
            TrackerService::class.java
        ).apply {
            this.action = action
            startService(this)
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = RunBottomSheet(this)
        bottomSheet.show(supportFragmentManager, RunBottomSheet.TAG)
    }

    /** Implementations: */
    override fun onContinueClick() {
        isContinued = true
        startRun()
    }

    override fun onStopClick() {
        sendActionCommandToService(ACTION_SERVICE_STOP)
        timer.stop()
        showSummarry()
    }

    private fun showSummarry() {
        val locations: ArrayList<LatLng> = ArrayList(locationList)
        val intent = Intent(this, SummaryActivity::class.java)
        intent.putExtra(TIME, timer.timeTextView.text.toString())
        intent.putExtra(AVG_PACE, getTimeInMinutesAndSeconds(avgPace))
        intent.putExtra(DISTANCE, binding.distanceTextView.text.toString())
        intent.putParcelableArrayListExtra(LOCATIONS, locations)
        // TODO: send steps along with all the running data.
        startActivity(intent)
    }
}