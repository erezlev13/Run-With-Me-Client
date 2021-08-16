package com.runwithme.runwithme.view.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.adapters.GroupsAdapter
import com.runwithme.runwithme.databinding.FragmentGroupsBinding
import com.runwithme.runwithme.model.*
import com.runwithme.runwithme.utils.Constants.EXTRA_GROUP_DETAILS
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.view.activity.MainActivity
import com.runwithme.runwithme.viewmodels.GroupViewModel
import com.runwithme.runwithme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class GroupsFragment : Fragment() {

    private lateinit var binding: FragmentGroupsBinding
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var currentUser : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.readUser.observeOnce(requireActivity(),{userList ->
            if(userList.isNotEmpty()){
                currentUser = userList[0].user
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentGroupsBinding.inflate(layoutInflater)
        (requireActivity() as MainActivity).supportActionBar?.hide()

        binding.createGroupButton.setOnClickListener {
            if(currentUser.friends.size > 0){
                Intent(requireContext(), CreateGroupActivity::class.java).also {
                    startActivity(it)
                }
            }
            else{
                Snackbar.make(binding.createGroupButton, "Please add friends before trying to create new group",
                    Snackbar.LENGTH_LONG).show()
            }
        }

        getGroupListFromDB()
        return binding.root
    }

    private fun getGroupListFromDB() {
        var groupList : ArrayList<Group> = ArrayList()
        groupViewModel.getMyGroups()
        groupViewModel.myGroupsResponse.observeOnce(this,{response ->
            when(response){
                is NetworkResult.Success -> {
                    if(response.data?.groups != null) {
                        groupList = response.data.groups
                        if (groupList.size > 0) {
                            binding.myGroupsRecyclerView.visibility = View.VISIBLE
                            binding.noGroupsAvailableTextView.visibility = View.INVISIBLE
                            setupGroupsRecyclerView(groupList)
                        }else{
                            binding.myGroupsRecyclerView.visibility = View.INVISIBLE
                            binding.noGroupsAvailableTextView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun setupGroupsRecyclerView(groupList: ArrayList<Group>) {

        binding.myGroupsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.myGroupsRecyclerView.setHasFixedSize(true)
        val groupsAdapter = GroupsAdapter(groupList)
        binding.myGroupsRecyclerView.adapter = groupsAdapter

        groupsAdapter.setOnClickListener(object :
            GroupsAdapter.OnClickListener {
            override fun onClick(position: Int, group: Group) {
                val intent = Intent(activity, GroupDetailActivity::class.java)
                intent.putExtra(EXTRA_GROUP_DETAILS, group) // Passing the complete serializable data class to the detail activity using intent.
                startActivity(intent)
            }
        })
    }
}