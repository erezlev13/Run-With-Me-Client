package com.runwithme.runwithme.utils

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

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
    fun View.show(){
        this.visibility = View.VISIBLE
    }
    fun View.hide(){
        this.visibility = View.GONE
    }

    fun <T> MutableLiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}