package com.runwithme.runwithme.utils

object Constants {
    // Permissions
    const val PERMISSION_LOCATION_REQUEST_CODE = 1
    const val PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 2
    const val PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE = 3

    // Notifications
    const val NOTIFICATION_CHANNEL_ID = "tracker_notification_id"
    const val NOTIFICATION_CHANNEL_NAME = "tracker_notification"
    const val NOTIFICATION_ID = 3

    const val PENDING_INTENT_REQUEST_CODE = 99

    // Service
    const val ACTION_SERVICE_START = "Start Action Service"
    const val ACTION_SERVICE_STOP = "Stop Action Service"

    // Location
    const val LOCATION_UPDATE_INTERVAL = 4000L
    const val LOCATION_FASTEST_UPDATE_INTERVAL = 2000L

    //Retrofit
    const val BASE_URL = "http://10.0.2.2:3000/run-with-me/"
    const val MAPS_STATIC_URL = "https://maps.googleapis.com/maps/api/staticmap?"

    //Room
    const val DATABASE_NAME = "run_with_me_database"
    const val USER_TABLE = "user_table"

    //Preferences
    const val USER_TOKEN = "user_token"

    const val IMAGE_DIRECTORY = "RunWithMeImages"

    // Run Data
    const val TIME = "Time"
    const val AVG_PACE = "Avg Pace"
    const val DISTANCE = "Distance"
    const val STEPS = "Steps"
}