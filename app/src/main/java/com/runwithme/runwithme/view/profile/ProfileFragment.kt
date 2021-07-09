package com.runwithme.runwithme.view.profile


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.databinding.FragmentProfileBinding
import com.runwithme.runwithme.utils.Permissions.hasExternalStoragePermission
import com.runwithme.runwithme.utils.Permissions.requestExternalStoragePermission
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(),EasyPermissions.PermissionCallbacks{

    lateinit var binding: FragmentProfileBinding
    private var currentPhotoPath: Uri? = null
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if(result.data != null){
                currentPhotoPath = result.data!!.data
            }
            try {
                @Suppress("DEPRECATION")
                val selectedImageBitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, currentPhotoPath)
                binding.profileImage.setImageBitmap(selectedImageBitmap)

            } catch (e: java.lang.RuntimeException) {
                Snackbar.make(binding.root, "Oops something went wrong... Please try again", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.pickPhotoActionButton.setOnClickListener{
            if (hasExternalStoragePermission(requireContext())) {
                uploadImageFromPhotoLibrary()
            } else {
                requestExternalStoragePermission(this)
            }
        }
        binding.buttonStatistics.setOnClickListener {
            Intent(requireContext(), RunStatActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return binding.root
    }

    private fun uploadImageFromPhotoLibrary() {
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        // Get photo from gallery.
        val getFromGalleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        resultLauncher.launch(Intent.createChooser(getFromGalleryIntent, "Select Image"))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else{
            requestExternalStoragePermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        uploadImageFromPhotoLibrary()
    }




}