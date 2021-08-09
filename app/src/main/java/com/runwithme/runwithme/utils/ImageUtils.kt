package com.runwithme.runwithme.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.*
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun resizeBitmap(source: Bitmap): Bitmap {
        val maxResolution = 200    //edit 'maxResolution' to fit your need
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

    fun bitmapToEncodedString(bitmap: Bitmap) : String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return encodeToString(imageBytes, DEFAULT)
    }
    fun encodedStringToBitmap(encodedString : String) : Bitmap {
        val imgBytes: ByteArray = decode(encodedString, DEFAULT);
        return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
    }
}