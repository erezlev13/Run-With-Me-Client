package com.runwithme.runwithme.utils

import android.graphics.Bitmap

object ImageUtils {
    fun resizeBitmap(source: Bitmap): Bitmap {
        val maxResolution = 1000    //edit 'maxResolution' to fit your need
        val width = source.width
        val height = source.height
        var newWidth = width
        var newHeight = height
        val rate: Float

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / width.toFloat()
                newHeight = (height * rate).toInt()
                newWidth = maxResolution
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / height.toFloat()
                newWidth = (width * rate).toInt()
                newHeight = maxResolution
            }
        }
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true)
    }
}