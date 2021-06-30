package com.runwithme.runwithme.view.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.utils.MapUtils.setCameraPosition
import com.runwithme.runwithme.utils.Permissions.hasLocationPermission
import com.runwithme.runwithme.utils.Permissions.requestBackgroundLocationPermission
import com.runwithme.runwithme.utils.Permissions.requestLocationPermission
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

/**
 * A simple [Fragment] subclass.
 */
class RunFragment : Fragment(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    /** Properties: */
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var runLayoutContainer: View

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
        val view =  inflater.inflate(R.layout.fragment_run, container, false)
        runLayoutContainer = view.findViewById(R.id.map)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        if (hasLocationPermission(requireContext())) {
            showCurrentLocation()
        } else {
            requestLocationPermission(this)
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
            } else {
                val noGPSMsg = Snackbar.make(runLayoutContainer, "No GPS signal yet", Snackbar.LENGTH_INDEFINITE)
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