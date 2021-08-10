package com.runwithme.runwithme.view.groups

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.adapters.SelectFriendAdapter
import com.runwithme.runwithme.databinding.ActivityCreateGroupBinding
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.model.network.GroupDataRequest
import com.runwithme.runwithme.utils.Constants.NAV_TO_GROUPS
import com.runwithme.runwithme.utils.Constants.NO_CONNECTION
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.ImageUtils.bitmapToEncodedString
import com.runwithme.runwithme.utils.ImageUtils.resizeBitmap
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.utils.Permissions
import com.runwithme.runwithme.view.activity.MainActivity
import com.runwithme.runwithme.viewmodels.GroupViewModel
import com.runwithme.runwithme.viewmodels.UserViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateGroupActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityCreateGroupBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var friendList: ArrayList<User>
    private var checkedFriendList: ArrayList<String> = ArrayList()
    private var selectedImageBitmap : Bitmap? = null
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var photoPath : Uri? = null
            if(result.data != null){
                photoPath = result.data!!.data!!
            }
            try {
                @Suppress("DEPRECATION")
                selectedImageBitmap =
                    resizeBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, photoPath))
                binding.groupImageView.setImageBitmap(selectedImageBitmap)


            } catch (e: java.lang.RuntimeException) {
                Snackbar.make(binding.root, "something went wrong... Please try again", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)

        binding.groupImageView.setOnClickListener{
            if (Permissions.hasExternalStoragePermission(this)) {
                uploadImageFromPhotoLibrary()
            } else {
                Permissions.activityRequestExternalStoragePermission(this)
            }
        }
        binding.createGroupButton.setOnClickListener {
            onClickCreateGroupButton()
        }


        setSupportActionBar(binding.createGroupToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        binding.createGroupToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllFriendsFromDB()

    }
    private fun onClickCreateGroupButton(){
        var nameFilled : Boolean = true
        var descriptionFilled : Boolean = true
        if(binding.groupNameTextInputEditText.text!!.isEmpty()){
            binding.groupNameTextInputLayout.error = getString(R.string.group_name_empty_error)
            nameFilled = false
        }
        if(binding.groupDescriptionTextInputEditText.text!!.isEmpty()){
            binding.groupDescriptionTextInputLayout.error = getString(R.string.group_description_empty_error)
            descriptionFilled = false
        }
        if(nameFilled && descriptionFilled ){
            binding.groupNameTextInputLayout.error = null
            binding.groupDescriptionTextInputLayout.error = null

            createGroup()
        }

    }

    private fun createGroup() {
        val groupName= binding.groupNameTextInputEditText.text.toString()
        val groupDescription = binding.groupDescriptionTextInputEditText.text.toString()
        var groupImageString : String = ""
        if(selectedImageBitmap != null){
            groupImageString = bitmapToEncodedString(resizeBitmap(selectedImageBitmap!!))
        }


        groupViewModel.saveGroupData(GroupDataRequest(groupName,groupDescription,groupImageString,checkedFriendList))
        binding.createGroupProgressBar.show()
        groupViewModel.groupData.observeOnce(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Snackbar.make(binding.createGroupButton, "Group created successfully ", Snackbar.LENGTH_LONG).show()
                    getBackToGroupsFragment()
                }
                is NetworkResult.Error ->{
                    if(response.message == NO_CONNECTION){
                        Snackbar.make(binding.createGroupButton, "No internet connection", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun getAllFriendsFromDB() {
        userViewModel.readUser.observeOnce(this,{userList ->
            if(userList.isNotEmpty()){
                userViewModel.getAllFriends()
                userViewModel.myFriendsResponse.observeOnce(this, { response ->
                    when(response){
                        is NetworkResult.Success -> {
                            if(response.data?.friends != null) {
                                friendList = response.data.friends
                                setupMyFriendRecyclerView()
                            }
                        }

                    }
                })
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            SettingsDialog.Builder(this).build().show()
        } else{
            Permissions.activityRequestExternalStoragePermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        uploadImageFromPhotoLibrary()
    }

    private fun setupMyFriendRecyclerView() {
        binding.myFriendsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.myFriendsRecyclerView.setHasFixedSize(true)
        val selectFriendAdapter = SelectFriendAdapter(friendList)
        binding.myFriendsRecyclerView.adapter = selectFriendAdapter

        selectFriendAdapter.setOnCheckListener(object :
            SelectFriendAdapter.OnCheckListener {
            override fun onCheck(position: Int, model: User) {
                if(!checkedFriendList.contains(model._id)){
                    checkedFriendList.add(model._id)
                }
            }
        })
        selectFriendAdapter.setOnUncheckListener(object :
            SelectFriendAdapter.OnUncheckListener {
            override fun onUncheck(position: Int, model: User) {
                if(checkedFriendList.contains(model._id)){
                    checkedFriendList.remove(model._id)
                }
            }
        })
    }

    private fun getBackToGroupsFragment() {
        binding.createGroupProgressBar.hide()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(NAV_TO_GROUPS,true)
        startActivity(intent)
    }


}