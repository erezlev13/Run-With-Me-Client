package com.runwithme.runwithme.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    private lateinit var binding: ActivityShowAndDeleteFriendBinding
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mFriendList: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAndDeleteFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setSupportActionBar(binding.showAndDeleteFriendToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        binding.showAndDeleteFriendToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllFriendsFromDB()
    }

    private fun getAllFriendsFromDB() {
        mUserViewModel.readUser.observeOnce(this, { userList ->
            if (userList.isNotEmpty()) {
                mUserViewModel.getAllFriends()
                mUserViewModel.myFriendsResponse.observeOnce(this, { response ->
                    when (response) {
                        is NetworkResult.Success -> {
                            if (response.data?.friends != null) {
                                mFriendList = response.data.friends
                                if (mFriendList.size > 0) {
                                    binding.friendsRecyclerView.visibility = View.VISIBLE
                                    binding.noFriendsAvailableTextView.visibility = View.INVISIBLE
                                    setupShowAndDeleteFriendRecyclerView()
                                } else {
                                    binding.friendsRecyclerView.visibility = View.INVISIBLE
                                    binding.noFriendsAvailableTextView.visibility = View.VISIBLE
                                }
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
        val showAndDeleteFriendsAdapter = ShowAndDeleteFriendsAdapter(mFriendList)
        binding.friendsRecyclerView.adapter = showAndDeleteFriendsAdapter

        showAndDeleteFriendsAdapter.setOnClickListener(object :
            ShowAndDeleteFriendsAdapter.OnClickListener {
            override fun onClick(position: Int, model: User) {
                deleteFriend(model)
            }
        })
    }

    private fun deleteFriend(Friend: User) {
        mUserViewModel.readUser.observeOnce(this, { userList ->
            if (userList.isNotEmpty()) {
                val user = userList[0].user
                user.friends.remove(Friend._id)
                val updatedUserEntity = UserEntity(userList[0].token, user)
                mUserViewModel.deleteFriend(updatedUserEntity, Friend._id)
            }
        })
    }
}