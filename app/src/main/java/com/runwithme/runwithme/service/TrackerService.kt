package com.runwithme.runwithme.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.runwithme.runwithme.R
import com.runwithme.runwithme.model.Timer
import com.runwithme.runwithme.utils.Constants.ACTION_SERVICE_START
import com.runwithme.runwithme.utils.Constants.ACTION_SERVICE_STOP
import com.runwithme.runwithme.utils.Constants.LOCATION_FASTEST_UPDATE_INTERVAL
import com.runwithme.runwithme.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.runwithme.runwithme.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.runwithme.runwithme.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.runwithme.runwithme.utils.Constants.NOTIFICATION_ID
import com.runwithme.runwithme.utils.MapUtils.calculateTheDistance
import com.runwithme.runwithme.utils.MapUtils.getDistance
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "TrackerService"

@AndroidEntryPoint
class TrackerService : LifecycleService() {

    /** Properties: */
    @Inject
    lateinit var notification: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var totalDistance: Double = 0.0

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.locations?.let { locations ->
                for (location in locations) {
                    updateLocationList(location)
                    updateNotificationPeriodically()
                }
            }
        }
    }

    /** Service Methods: */
    override fun onCreate() {
        Log.d(TAG, "onCreate: called")
        initValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: called")
        intent?.let {
            when (it.action) {
                ACTION_SERVICE_START -> {
                    Log.d(TAG, "onStartCommand: ACTION_SERVICE_START")
                    isStarted.postValue(true)
                    startLocationUpdates()
                    startForegroundService()
                }
                ACTION_SERVICE_STOP -> {
                    Log.d(TAG, "onStartCommand: ACTION_SERVICE_STOP")
                    isStarted.postValue(false)
                    stopForegroundService()
                }
                else -> {
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /** Class Methods: */
    private fun initValues() {
        isStarted.postValue(false)
        locationList.postValue(mutableListOf())
    }

    private fun startForegroundService() {
        Log.d(TAG, "startForegroundService: start")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun stopForegroundService() {
        Log.d(TAG, "startForegroundService: stop")
        removeLocationUpdates()
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
        stopForeground(true)
        stopSelf()
    }

    private fun updateLocationList(location: Location) {
        val newLatLng = LatLng(location.latitude, location.longitude)
        locationList.value?.apply {
            add(newLatLng)
            locationList.postValue(this)
        }
    }

    private fun updateNotificationPeriodically() {
        notification.apply {
            setContentTitle("Distance Travelled")
            locationList.value?.let { totalDistance += calculateTheDistance(it) }
            setContentText(getDistance(totalDistance) + "km")
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        var isStarted = MutableLiveData<Boolean>()
        var locationList = MutableLiveData<MutableList<LatLng>>()
    }
}