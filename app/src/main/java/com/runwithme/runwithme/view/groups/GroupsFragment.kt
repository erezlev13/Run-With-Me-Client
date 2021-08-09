package com.runwithme.runwithme.view.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.runwithme.runwithme.adapters.GroupsAdapter
import com.runwithme.runwithme.databinding.FragmentGroupsBinding
import com.runwithme.runwithme.model.*
import com.runwithme.runwithme.utils.Constants.EXTRA_GROUP_DETAILS
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.viewmodels.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class GroupsFragment : Fragment() {

    private lateinit var binding: FragmentGroupsBinding
    private lateinit var groupViewModel: GroupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentGroupsBinding.inflate(layoutInflater)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


        binding.createGroupButton.setOnClickListener {
            Intent(requireContext(), CreateGroupActivity::class.java).also {
                startActivity(it)
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
            override fun onClick(position: Int, model: Group) {
                val intent = Intent(activity, GroupDetailActivity::class.java)
                intent.putExtra(EXTRA_GROUP_DETAILS, model) // Passing the complete serializable data class to the detail activity using intent.
                startActivity(intent)
            }
        })

    }
}