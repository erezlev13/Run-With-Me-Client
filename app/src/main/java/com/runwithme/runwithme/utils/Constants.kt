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

    const val EXTRA_GROUP_DETAILS = "extra_group_details"
    const val EXTRA_RUN_DETAILS = "extra_run_details"
    const val NAV_TO_GROUPS = "groups"

    //Retrofit
    const val BASE_URL = "http://192.168.0.107:3000/run-with-me/"
    const val MAPS_STATIC_URL = "https://maps.googleapis.com/maps/api/staticmap?"

    //Room
    const val DATABASE_NAME = "run_with_me_database"
    const val USER_TABLE = "user_table"

    //Preferences
    const val USER_TOKEN = "user_token"

    //Group Run
    const val GROUP_RUN = "GroupRun"
    const val GROUP_RUN_ID = "GroupId"

    // Run Data
    const val START_TIME = "Start Time"
    const val END_TIME = "End Time"
    const val TIME = "Time"
    const val AVG_PACE = "Avg Pace"
    const val DISTANCE = "Distance"
    const val LOCATIONS = "Locations"
    const val WAY_POINTS = "WayPoints"
    const val RUN_TYPE = "RunType"
    const val STEPS = "Steps"
    const val GROUP_ID = "Group ID"

    // Response Results
    const val NO_CONNECTION = "No Connection"
}