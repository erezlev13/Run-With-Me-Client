package com.runwithme.runwithme.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivityGroupDetailBinding
import com.runwithme.runwithme.databinding.ActivityStatisticsDetailsBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.utils.Constants
import com.runwithme.runwithme.utils.MapUtils
import com.runwithme.runwithme.utils.TimeUtils
import java.time.Duration

class StatisticsDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mRunDetails : Run? = null
    private lateinit var binding: ActivityStatisticsDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_statistics_details) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (intent.hasExtra(Constants.EXTRA_RUN_DETAILS)) {
            mRunDetails =
                intent.getSerializableExtra(Constants.EXTRA_RUN_DETAILS) as Run
        }

        if(mRunDetails != null){
            setupRunDetails()
        }
    }

    private fun setupRunDetails(){
        binding.statisticsDetailsDistanceSummaryTextView.text = mRunDetails!!.runData.distance.toString() + "Km"
        binding.statisticsDetailsPaceTextView.text = mRunDetails!!.runData.averagePace
        binding.statisticsDetailsStepsTextView.text = mRunDetails!!.runData.steps.toString()
        binding.statisticsDetailsTimeTextView.text = TimeUtils.calculateTimeDifference(mRunDetails!!.startTime
            ,mRunDetails!!.endTime)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLngList = coordinatesToLatLngList()
        showPolyline(latLngList)

        if (latLngList.isNotEmpty()) {
            mMap.addMarker(
                MarkerOptions()
                .icon(MapUtils.createCustomMarker(this))
                .position(latLngList.first())
            )
        }

        // Set the camera to be at the starting point position, with large zoom so we could see the full course.
        mMap.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(CameraPosition(latLngList.first(), 12f, 0f, 0f)),
            2000,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    // Leave this empty
                }

                override fun onCancel() {
                    Snackbar.make(binding.root, "Oops... something went wrong", Snackbar.LENGTH_LONG)
                }
            }
        )

    }

    private fun showPolyline(latLngList : ArrayList<LatLng>) {
        mMap.addPolyline(
            PolylineOptions()
                .color(R.color.colorPrimary)
                .clickable(false)
                .width(5f)
                .addAll(latLngList)
        )
    }

    private fun coordinatesToLatLngList() : ArrayList<LatLng>{
        var latLngList : ArrayList<LatLng> = ArrayList()
        val coordinates = mRunDetails!!.runData.route.coordinates
        coordinates.forEach{
            latLngList.add(LatLng(it[0],it[1]))
        }
        
        return latLngList
    }

}