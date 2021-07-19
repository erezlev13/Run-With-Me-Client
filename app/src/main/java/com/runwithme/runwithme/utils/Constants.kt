package com.runwithme.runwithme.utils

object Constants {
    const val PERMISSION_LOCATION_REQUEST_CODE = 1
    const val PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 2
    const val PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE = 3

    const val NOTIFICATION_CHANNEL_ID = "tracker_notification_id"
    const val NOTIFICATION_CHANNEL_NAME = "tracker_notification"
    const val NOTIFICATION_ID = 3

    const val PENDING_INTENT_REQUEST_CODE = 99

    const val ACTION_SERVICE_START = "Start Action Service"
    const val ACTION_SERVICE_STOP = "Stop Action Service"

    const val LOCATION_UPDATE_INTERVAL = 4000L
    const val LOCATION_FASTEST_UPDATE_INTERVAL = 2000L

    //Retrofit
    const val BASE_URL = "http://10.0.2.2:3000/run-with-me/"

    //Room
    const val DATABASE_NAME = "run_with_me_database"
    const val USER_TABLE = "user_table"

    //Preferences
    const val USER_TOKEN = "user_token"

    const val IMAGE_DIRECTORY = "RunWithMeImages"
}