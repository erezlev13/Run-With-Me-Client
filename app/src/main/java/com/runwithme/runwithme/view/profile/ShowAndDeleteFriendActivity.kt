package com.runwithme.runwithme.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.runwithme.runwithme.adapters.ShowAndDeleteFriendsAdapter
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.databinding.ActivityShowAndDeleteFriendBinding
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowAndDeleteFriendActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShowAndDeleteFriendBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var friendList: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAndDeleteFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setSupportActionBar(binding.showAndDeleteFriendToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        binding.showAndDeleteFriendToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllFriendsFromDB()
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
                                setupShowAndDeleteFriendRecyclerView()
                            }
                        }

                    }
                })
            }
        })

    }

    private fun setupShowAndDeleteFriendRecyclerView() {
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.friendsRecyclerView.setHasFixedSize(true)
        val showAndDeleteFriendsAdapter = ShowAndDeleteFriendsAdapter(friendList)
        binding.friendsRecyclerView.adapter = showAndDeleteFriendsAdapter

        showAndDeleteFriendsAdapter.setOnClickListener(object :
            ShowAndDeleteFriendsAdapter.OnClickListener {
            override fun onClick(position: Int, model: User) {
                deleteFriend(model)
            }
        })
    }

    private fun deleteFriend(Friend: User){
        userViewModel.readUser.observeOnce(this,{userList ->
            if(userList.isNotEmpty()){
                val user = userList[0].user
                user.friends.remove(Friend._id)
                val updatedUserEntity = UserEntity(userList[0].token,user)
                userViewModel.deleteFriend(updatedUserEntity,Friend._id)
            }
        })
    }


}