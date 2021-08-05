package com.runwithme.runwithme.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.runwithme.runwithme.R

class StatisticsDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_details)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // TODO: update markers and polyline on the map.
    }

    // TODO: get all data from RunStatActivity, and update it on the text views.
}