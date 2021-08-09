package com.runwithme.runwithme.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.runwithme.runwithme.utils.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.runwithme.runwithme.utils.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.runwithme.runwithme.utils.Constants.PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions

object Permissions {
    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    fun requestLocationPermission(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "This application cannot work without Location Permission",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun hasBackgroundLocationPermission(context: Context): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
        return true
    }

    fun requestBackgroundLocationPermission(fragment: Fragment){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                fragment,
                "Background location permission is essential to this application. Without it we will not be able to provide you with our service.",
                PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
    fun hasExternalStoragePermission(context: Context) = EasyPermissions.hasPermissions(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun requestExternalStoragePermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "External permission is essential for this app, so you can upload photos",
            PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
    fun activityRequestExternalStoragePermission(activity : Activity) {
        EasyPermissions.requestPermissions(
            activity,
            "External permission is essential for this app, so you can upload photos",
            PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

}