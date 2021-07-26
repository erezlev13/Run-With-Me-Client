package com.runwithme.runwithme.view.run

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.FragmentRunBinding
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.MapUtils.createCustomMarker
import com.runwithme.runwithme.utils.MapUtils.setCameraPosition
import com.runwithme.runwithme.utils.Permissions.hasBackgroundLocationPermission
import com.runwithme.runwithme.utils.Permissions.hasLocationPermission
import com.runwithme.runwithme.utils.Permissions.requestBackgroundLocationPermission
import com.runwithme.runwithme.utils.Permissions.requestLocationPermission
import com.runwithme.runwithme.view.activity.OnRunningActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

/**
 * A simple [Fragment] subclass.
 */
class RunFragment :
        Fragment(),
        OnMapReadyCallback,
        EasyPermissions.PermissionCallbacks {

    /** Properties: */
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var _binding: FragmentRunBinding? = null
    private val binding: FragmentRunBinding get() = _binding!!
    private lateinit var runAnimation: AnimationDrawable

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentRunBinding.inflate(inflater, container, false)

        binding.runProgressBar.apply {
            setBackgroundResource(R.drawable.run_anim)
            runAnimation = background as AnimationDrawable
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        onStartClick()
    }

    private fun onStartClick() {
        // Starting running activity by tapping on "start".
        // Start new activity, that will show all measurements.
        binding.startRunningButton.setOnClickListener {
            binding.runProgressBar.show()
            runAnimation.start()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.runProgressBar.hide()
                runAnimation.stop()
                Intent(requireContext(), OnRunningActivity::class.java).also {
                    startActivity(it)
                }
            }, 3000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Maps Implementations: */
    /** --------------------- */

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        hideUiSettings()

        if (hasLocationPermission(requireContext()) && hasBackgroundLocationPermission(requireContext())) {
            showCurrentLocation()
        } else {
            requestLocationPermission(this)
            requestBackgroundLocationPermission(this)
        }
    }

    /** Class Methods: */
    private fun hideUiSettings() {
        mMap.uiSettings.isMapToolbarEnabled = false
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                val lastKnownLocation = LatLng(
                    it.result.latitude,
                    it.result.longitude
                )

                mMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        setCameraPosition(lastKnownLocation)
                    )
                )

                mMap.addMarker(
                    MarkerOptions()
                        .position(lastKnownLocation)
                        .icon(createCustomMarker(requireContext()))
                )
            } else {
                val noGPSMsg = Snackbar.make(binding.runContainer, "No GPS signal yet", Snackbar.LENGTH_INDEFINITE)
                noGPSMsg.setAction("OK") {
                    noGPSMsg.duration = 1
                }
            }
        }
    }

    /** Permissions: */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        showCurrentLocation()
    }
}