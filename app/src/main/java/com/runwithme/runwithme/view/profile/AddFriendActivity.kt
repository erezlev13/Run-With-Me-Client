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
    private lateinit var friendSearchAdapter: FriendSearchAdapter
    private var userList: List<User> = ArrayList<User>()
    private lateinit var userViewModel: UserViewModel
    private lateinit var  currentUser :User

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

        friendSearchAdapter = FriendSearchAdapter( ArrayList<User>())
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.friendTextInputEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                filterFriends(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })

        getAllUserFromDB()
        setupRunStatisticsRecyclerView()

        userViewModel.readUser.observeOnce(this,{userList ->
            if(userList.isNotEmpty()){
                currentUser = userList[0].user
            }
        })
    }

    private fun getAllUserFromDB(){
        userViewModel.getAllUsers()
        userViewModel.allUsersResponse.observeOnce(this, { response ->
            when(response){
                is NetworkResult.Success -> {
                    if(response.data?.users != null) {
                        userList = response.data.users
                    }
                }

            }
        })
    }

    private fun filterFriends(text: String) {
        val filteredList : ArrayList<User> = ArrayList()
        if(!text.isEmpty()){
            for(user: User in userList){
                if(!currentUser._id.equals(user._id) && !isExistInFriendList(user._id)){
                    if(user.firstName.toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(user)
                    }
                }

            }
        }
        friendSearchAdapter.filterList(filteredList)
    }

    private fun isExistInFriendList(friendId :String) : Boolean{
        for(userId in currentUser.friends){
            if(userId.equals(friendId)){
                return true
            }
        }
        return false
    }

    private fun setupRunStatisticsRecyclerView() {

        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.friendsRecyclerView.setHasFixedSize(true)
        binding.friendsRecyclerView.adapter = friendSearchAdapter

        friendSearchAdapter.setOnClickListener(object :
            FriendSearchAdapter.OnClickListener {
            override fun onClick(position: Int, model: User) {
                addFriend(model)
            }
        })

    }
    private fun addFriend(newFriend: User){
        userViewModel.readUser.observeOnce(this,{database ->
            if(database.isNotEmpty()){
                val user = database[0].user
                user.friends.add(newFriend._id)
                val updatedUserEntity = UserEntity(database[0].token,user)
                userViewModel.addFriend(updatedUserEntity,newFriend._id)
            }
        })
    }
}