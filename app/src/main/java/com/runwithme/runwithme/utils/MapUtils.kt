package com.runwithme.runwithme.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.runwithme.runwithme.R
import java.io.IOException
import java.text.DecimalFormat

object MapUtils {
    fun setCameraPosition(location: LatLng): CameraPosition {
        return CameraPosition.Builder()
            .target(location)
            .zoom(18f)
            .build()
    }

    fun calculateTheDistance(locationList: MutableList<LatLng>): Double {
        var kmResult = 0.0
        if(locationList.size > 1){
            val meters =
                SphericalUtil.computeDistanceBetween(locationList[locationList.lastIndex - 1], locationList.last())
            val kilometers = meters / 1000
            kmResult = kilometers
        }
        return kmResult
    }

    fun getDistance(km: Double): String {
        return if (km != 0.0) {
            DecimalFormat("#.##").format(km)
        } else {
            "0.00"
        }
    }

    fun createCustomMarker(context: Context?): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_location)
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.draw(canvas)

            return BitmapDescriptorFactory.fromBitmap(bitmap)
        } else {
            throw NullPointerException("There was no bitmap to create.")
        }
    }
}