package com.runwithme.runwithme.view.activity.summary

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.graphics.BitmapCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivitySummaryBinding
import com.runwithme.runwithme.model.RunType
import com.runwithme.runwithme.model.network.RunDataRequest
import com.runwithme.runwithme.utils.Constants.AVG_PACE
import com.runwithme.runwithme.utils.Constants.DISTANCE
import com.runwithme.runwithme.utils.Constants.END_TIME
import com.runwithme.runwithme.utils.Constants.LOCATIONS
import com.runwithme.runwithme.utils.Constants.NO_CONNECTION
import com.runwithme.runwithme.utils.Constants.START_TIME
import com.runwithme.runwithme.utils.Constants.TIME
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.MapUtils.createCustomMarker
import com.runwithme.runwithme.view.activity.MainActivity
import com.runwithme.runwithme.viewmodels.RunViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SummaryActivity : AppCompatActivity(), OnMapReadyCallback {

    /** Properties: */
    private lateinit var binding: ActivitySummaryBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mViewModel: RunViewModel

    private lateinit var startTime: LocalTime
    private lateinit var endTime: LocalTime
    private lateinit var avgPace: SimpleDateFormat
    private var distance: Float = 0f
    private var steps: Int = 0
    private var locations: ArrayList<LatLng> = ArrayList()
    private var locationsPair: ArrayList<Pair<Double, Double>> = ArrayList()
    private lateinit var runType: RunType

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

        // Set view model.
        mViewModel = ViewModelProvider(this).get(RunViewModel::class.java)

        // Get all data from intent (RunningOn Activity) and set it to the views in this screen.
        // Listen to click on save buttons.
        getDataAndSetViews()

        // Set progress bar and map.
        binding.summaryProgressBar.show()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_summary) as SupportMapFragment
        mapFragment.getMapAsync(this)

        onSaveClickListener()
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

        mMap.uiSettings.apply {
            setAllGesturesEnabled(false)
        }

        Log.d("Summary", "${locations.first()}")
        mMap.addMarker(MarkerOptions()
            .icon(createCustomMarker(this))
            .position(locations.first())
        )

        mMap.animateCamera(
            CameraUpdateFactory
            .newCameraPosition(CameraPosition(locations.first(), 15f, 0f, 0f)),
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

    /** Menu Methods: */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.summary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val isSelected: Boolean

        when (item.itemId) {
            R.id.delete_summary_menu_item -> {
                // Don't save those running details and continue to the main activity.
                getBackToMainActivity()
                isSelected = true
            }

            else -> isSelected = false
        }

        return isSelected
    }

    /** Class Methods: */
    private fun getDataAndSetViews() {
        startTime = LocalTime.parse(intent.getStringExtra(START_TIME))
        endTime = LocalTime.parse(intent.getStringExtra(END_TIME))
        avgPace = SimpleDateFormat(intent.getStringExtra(AVG_PACE), Locale.US)
        distance = intent.getStringExtra(DISTANCE)!!.toFloat()
        locations = intent.getParcelableArrayListExtra(LOCATIONS)
        setLocationsPair()

        binding.timeTextView.text = intent.getStringExtra(TIME)
        binding.paceTextView.text = intent.getStringExtra(AVG_PACE)
        binding.distanceSummaryTextView.text = intent.getStringExtra(DISTANCE)
    }

    private fun setLocationsPair() {
        locations.forEach {
            locationsPair.add(locations.indexOf(it), Pair(it.latitude, it.longitude))
        }
    }

    private fun onSaveClickListener() {
        binding.saveSummaryButton.setOnClickListener {
            saveRequest()
            hideProgressBar()
            observeResult()
        }
    }

    private fun saveRequest() {
        val request = RunDataRequest(
            startTime,
            endTime,
            LocalDateTime.now(),
            avgPace,
            distance,
            steps,
            locationsPair,
            RunType.PERSONAL
        )
        mViewModel.saveRunData(request)
    }

    private fun hideProgressBar() {
        binding.summaryProgressBar.show()
    }

    private fun observeResult() {
        mViewModel.runData.observeOnce(this) {
            Log.d("Summary", "${it.message}, ${it.data}")
            when {
                it.message == NO_CONNECTION -> {
                    binding.summaryProgressBar.hide()
                    Snackbar.make(binding.saveSummaryButton, "No internet connection", Snackbar.LENGTH_LONG).show()
                }
                it.data == null -> {
                    binding.summaryProgressBar.hide()
                    Snackbar.make(binding.saveSummaryButton, "Unsuccessful save. Please try again", Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    // There's connection and we got a runData object as a response.
                    binding.summaryProgressBar.hide()
                    Snackbar.make(binding.saveSummaryButton, "Successfully saved", Snackbar.LENGTH_LONG).show()
                    getBackToMainActivity()
                }
            }
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