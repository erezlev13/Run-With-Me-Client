package com.runwithme.runwithme.view.profile


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.*
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.databinding.FragmentProfileBinding
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ImageUtils.bitmapToEncodedString
import com.runwithme.runwithme.utils.ImageUtils.encodedStringToBitmap
import com.runwithme.runwithme.utils.ImageUtils.resizeBitmap
import com.runwithme.runwithme.utils.Permissions.hasExternalStoragePermission
import com.runwithme.runwithme.utils.Permissions.requestExternalStoragePermission
import com.runwithme.runwithme.view.activity.LoginActivity
import com.runwithme.runwithme.viewmodels.UserViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.*


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment(),EasyPermissions.PermissionCallbacks{

    private lateinit var binding: FragmentProfileBinding
    private var currentPhotoPath: Uri? = null
    private lateinit var userViewModel: UserViewModel

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

                updateUserPhoto(resizeBitmap(selectedImageBitmap))

            } catch (e: java.lang.RuntimeException) {
                Snackbar.make(binding.root, "something went wrong... Please try again", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

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
        binding.buttonAddFriend.setOnClickListener {
            Intent(requireContext(), AddFriendActivity::class.java).also {
                startActivity(it)
            }

        }
        binding.totalFriendsTextView.setOnClickListener {
            Intent(requireContext(),ShowAndDeleteFriendActivity::class.java).also{
                startActivity(it)
        }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding.profileToolbar.inflateMenu(R.menu.profile_menu)
        binding.profileToolbar.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.logout_item -> {
                    logout()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }
        setupProfileData()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupProfileData()
    }


    private fun logout(){
        deleteUserFromLocalDB()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun setupProfileData() {
        userViewModel.readUser.observe(requireActivity(),{database ->
            if(database.isNotEmpty()){
                val user = database[0].user
                if(!user.photoUri.isNullOrEmpty()){
                    binding.profileImage.setImageBitmap(encodedStringToBitmap(user.photoUri))
                }
                binding.totalRunsTextView.text = user.runs.size.toString()
                binding.runnerEmailTextView.text = user.email
                binding.runnerNameTextView.text = user.firstName + " " +user.lastName
                binding.totalFriendsTextView.text = user.friends.size.toString()
                binding.totalRunsTextView.text = user.runs.size.toString()

            }
        })
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

    private fun updateUserPhoto(bitmap: Bitmap){
        userViewModel.readUser.observeOnce(this,{database ->
            if(database.isNotEmpty()){
                val user = database[0].user
                user.photoUri = bitmapToEncodedString(bitmap)
                val updatedUserEntity = UserEntity(database[0].token,user)
                userViewModel.updateUser(updatedUserEntity)
            }
        })
    }

    private fun deleteUserFromLocalDB(){
        userViewModel.readUser.observeOnce(this,{ database ->
            if(database.isNotEmpty()){
                userViewModel.deleteUser(database[0])
            }
        })
    }







}