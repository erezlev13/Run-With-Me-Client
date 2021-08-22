package com.runwithme.runwithme.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.runwithme.runwithme.adapters.FriendSearchAdapter
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.databinding.ActivityAddFriendBinding
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding
    private lateinit var mFriendSearchAdapter: FriendSearchAdapter
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mCurrentUser: User
    private var mUserList: List<User> = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.addFriendToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        binding.addFriendToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        mFriendSearchAdapter = FriendSearchAdapter(ArrayList<User>())
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mUserViewModel.readUser.observeOnce(this, { userList ->
            if (userList.isNotEmpty()) {
                mCurrentUser = userList[0].user
            }
        })

        binding.friendTextInputEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                filterFriends(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

        getAllUserFromDB()


    }

    private fun getAllUserFromDB() {
        mUserViewModel.getAllUsers()
        mUserViewModel.allUsersResponse.observeOnce(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (response.data?.users != null) {
                        mUserList = response.data.users
                    }
                    setupAddFriendRecyclerView()
                }

            }
        })
    }

    private fun filterFriends(text: String) {
        val filteredList: ArrayList<User> = ArrayList()
        if (!text.isEmpty()) {
            for (user: User in mUserList) {
                if (!mCurrentUser._id.equals(user._id) && !isExistInFriendList(user._id)) {
                    if (user.firstName.toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(user)
                    }
                }

            }
        }
        mFriendSearchAdapter.filterList(filteredList)
    }

    private fun isExistInFriendList(friendId: String): Boolean {
        for (userId in mCurrentUser.friends) {
            if (userId.equals(friendId)) {
                return true
            }
        }
        return false
    }

    private fun setupAddFriendRecyclerView() {

        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.friendsRecyclerView.setHasFixedSize(true)
        binding.friendsRecyclerView.adapter = mFriendSearchAdapter

        mFriendSearchAdapter.setOnClickListener(object :
            FriendSearchAdapter.OnClickListener {
            override fun onClick(model: User) {
                addFriend(model)
            }
        })

    }

    private fun addFriend(newFriend: User) {
        mUserViewModel.readUser.observeOnce(this, { userList ->
            if (userList.isNotEmpty()) {
                val user = userList[0].user
                user.friends.add(newFriend._id)
                val updatedUserEntity = UserEntity(userList[0].token, user)
                mUserViewModel.addFriend(updatedUserEntity, newFriend._id)
            }
        })
    }
}