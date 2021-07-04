package com.runwithme.runwithme.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible

object ExtensionFunctions {
    fun TextView.show() {
        this.visibility = View.VISIBLE
        this.isVisible = true
    }

    fun TextView.hide() {
        this.visibility = View.GONE
        this.isVisible = false
    }

    fun ProgressBar.show() {
        this.visibility = View.VISIBLE
    }

    fun ProgressBar.hide() {
        this.visibility = View.GONE
    }
}