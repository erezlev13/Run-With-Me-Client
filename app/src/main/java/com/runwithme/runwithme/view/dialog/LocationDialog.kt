package com.runwithme.runwithme.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.LocationDialogBinding
import com.runwithme.runwithme.utils.MapUtils
import com.runwithme.runwithme.utils.Permissions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class LocationDialog(private val listener: OnLocationChoose) :
    DialogFragment(),
    OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks {

    private lateinit var binding: LocationDialogBinding
    private var chosenPosition: LatLng? = null

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentMarker: Marker

    interface OnLocationChoose {
        fun onLocationChooseSuccess(position: LatLng)
        fun onLocationChooseFailed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            binding = LocationDialogBinding.inflate(layoutInflater)
            builder.setView(binding.root)
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.choose_location_map) as SupportMapFragment
            mapFragment.getMapAsync(this)
            binding.saveLocationButton.setOnClickListener {
                if (chosenPosition != null) {
                    listener.onLocationChooseSuccess(chosenPosition!!)
                    dialog?.cancel()
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener {
            if (chosenPosition != null) {
                currentMarker.remove()
            }

            googleMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .icon(MapUtils.createCustomMarker(listener as Context))
            ).also { currentMarker = it }
            chosenPosition = it
        }

        if (Permissions.hasLocationPermission(requireContext()) && Permissions.hasBackgroundLocationPermission(
                requireContext()
            )
        ) {
            showCurrentLocation()
        } else {
            Permissions.requestLocationPermission(this)
            Permissions.requestBackgroundLocationPermission(this)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onLocationChooseFailed()
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
                        MapUtils.setCameraPosition(
                            lastKnownLocation
                        )
                    ),
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

            } else {
                val noGPSMsg = Snackbar.make(binding.saveLocationButton, "No GPS signal yet", Snackbar.LENGTH_INDEFINITE)
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
            Permissions.requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        showCurrentLocation()
    }

    companion object {
        const val TAG = "LocationDialog"
    }
}