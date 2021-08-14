package com.runwithme.runwithme.view.run

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivitySummaryBinding
import com.runwithme.runwithme.model.GroupRun
import com.runwithme.runwithme.model.RunType
import com.runwithme.runwithme.model.network.RunDataRequest
import com.runwithme.runwithme.utils.Constants
import com.runwithme.runwithme.utils.Constants.AVG_PACE
import com.runwithme.runwithme.utils.Constants.DISTANCE
import com.runwithme.runwithme.utils.Constants.END_TIME
import com.runwithme.runwithme.utils.Constants.LOCATIONS
import com.runwithme.runwithme.utils.Constants.NO_CONNECTION
import com.runwithme.runwithme.utils.Constants.RUN_TYPE
import com.runwithme.runwithme.utils.Constants.START_TIME
import com.runwithme.runwithme.utils.Constants.TIME
import com.runwithme.runwithme.utils.Constants.GROUP_RUN_ID
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.MapUtils.createCustomMarker
import com.runwithme.runwithme.view.activity.MainActivity
import com.runwithme.runwithme.viewmodels.RunViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SummaryActivity : AppCompatActivity(), OnMapReadyCallback {

    /** Properties: */
    private lateinit var binding: ActivitySummaryBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mViewModel: RunViewModel

    private lateinit var startTime: String
    private lateinit var endTime: String
    private lateinit var avgPace: String
    private lateinit var runType: RunType
    private var groupRunId : String? = null
    private var distance: Float = 0f
    private var steps: Int = 0
    private var locations: ArrayList<LatLng> = ArrayList()
    private var wayPoints: ArrayList<LatLng> = ArrayList()
    private var locationsPair: ArrayList<Pair<Double, Double>> = ArrayList()


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
    }

    override fun onBackPressed() {
        // Override this so the functionality won't work
        Snackbar.make(binding.saveSummaryButton, "Please save or delete this summary", Snackbar.LENGTH_LONG).show()
    }

    /** Map Methods: */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        showPolyline()
        addWayPoints()
        binding.summaryProgressBar.hide()

        mMap.uiSettings.apply {
            setAllGesturesEnabled(false)
        }

        // Add the starting point marker.
        if (locations.isNotEmpty()) {
            mMap.addMarker(MarkerOptions()
                .icon(createCustomMarker(this))
                .position(locations.first())
            )
        }

        // Set the camera to be at the starting point position, with large zoom so we could see the full course.
        mMap.animateCamera(
            CameraUpdateFactory
            .newCameraPosition(CameraPosition(locations.first(), 12f, 0f, 0f)),
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
        startTime = intent.getStringExtra(START_TIME)
        endTime = intent.getStringExtra(END_TIME)
        avgPace = intent.getStringExtra(AVG_PACE)
        runType = intent.getSerializableExtra(RUN_TYPE) as RunType
        distance = intent.getStringExtra(DISTANCE)!!.toFloat()
        locations = intent.getParcelableArrayListExtra(LOCATIONS)
        if (intent.hasExtra(GROUP_RUN_ID)) {
            groupRunId = intent.getStringExtra(GROUP_RUN_ID)
        }

        setLocationsPair()

        binding.timeTextView.text = intent.getStringExtra(TIME)
        binding.paceTextView.text = intent.getStringExtra(AVG_PACE)
        binding.distanceSummaryTextView.text = intent.getStringExtra(DISTANCE)
        binding.stepsTextView.text = "3728"
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
            LocalDateTime.now().toString(),
            avgPace,
            distance,
            steps,
            locationsPair,
            runType,
            groupRunId
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

    private fun addWayPoints() {
        wayPoints.forEach { wayPoint ->
            mMap.addMarker(MarkerOptions()
                .icon(createCustomMarker(this))
                .position(wayPoint)
            )
        }
    }
}