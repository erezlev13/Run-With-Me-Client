package com.runwithme.runwithme.view.activity.summary

import android.content.Intent
import android.hardware.camera2.CameraMetadata
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivitySummaryBinding
import com.runwithme.runwithme.utils.Constants.AVG_PACE
import com.runwithme.runwithme.utils.Constants.DISTANCE
import com.runwithme.runwithme.utils.Constants.LOCATIONS
import com.runwithme.runwithme.utils.Constants.TIME
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.view.activity.MainActivity

class SummaryActivity : AppCompatActivity(), OnMapReadyCallback {

    /** Properties: */
    private lateinit var binding: ActivitySummaryBinding
    private lateinit var mMap: GoogleMap
    private var locations: ArrayList<LatLng> = ArrayList()

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply toolbar.
        setSupportActionBar(binding.summaryToolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

        // Get all data from intent (RunningOn Activity) and set it to the views in this screen.
        // Listen to click on save buttons.
        getDataAndSetViews()
        onSaveClickListener()

        // Set progress bar and map.
        binding.summaryProgressBar.show()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        onMapClickListener()
    }

    override fun onBackPressed() {
        // Override this so the functionality won't work
        Snackbar.make(binding.saveSummaryButton, "Please save or delete this summary", Snackbar.LENGTH_LONG).show()
    }

    /** Map Methods: */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        showPolyline()
        binding.summaryProgressBar.hide()

        mMap.animateCamera(
            CameraUpdateFactory
            .newCameraPosition(CameraPosition(locations.first(), 16f, 0f, 0f))
        )
    }

    /** Menu Methods: */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("Summary", "onCreateOptionsMenu was called")
        menuInflater.inflate(R.menu.summary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("Summary", "onOptionsItemSelected was called")
        val isSelected: Boolean

        when (item.itemId) {
            R.id.delete_summary_menu_item -> {
                // Don't save those running details and continue to the main activity.
                Log.d("Summary", "delete menu item was clicked")
                getBackToMainActivity()
                isSelected = true
            }

            else -> isSelected = false
        }

        return isSelected
    }

    /** Class Methods: */
    private fun getDataAndSetViews() {
        binding.timeTextView.text = intent.getStringExtra(TIME)
        binding.paceTextView.text = intent.getStringExtra(AVG_PACE)
        binding.distanceSummaryTextView.text = intent.getStringExtra(DISTANCE)
        locations = intent.getParcelableArrayListExtra(LOCATIONS)
    }

    private fun onSaveClickListener() {
        binding.saveSummaryButton.setOnClickListener {
            // TODO: Save the data of the current running on server.
            getBackToMainActivity()
        }
    }

    private fun getBackToMainActivity() {
        Log.d("Summary", "getBackToMainActivity was called")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showPolyline() {
        mMap.addPolyline(
            PolylineOptions()
                .color(R.color.colorPrimary)
                .clickable(false)
                .width(5f)
                .addAll(locations)
        )
    }

    private fun onMapClickListener() {
        // TODO: move to full map presentation screen.
    }
}