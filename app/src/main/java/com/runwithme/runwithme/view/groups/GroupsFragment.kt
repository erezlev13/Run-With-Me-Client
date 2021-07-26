package com.runwithme.runwithme.view.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.runwithme.runwithme.adapters.GroupsAdapter
import com.runwithme.runwithme.adapters.RunsStatisticsAdapter
import com.runwithme.runwithme.databinding.FragmentGroupsBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.model.Route
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.RunData
import com.runwithme.runwithme.utils.Constants.EXTRA_GROUP_DETAILS
import com.runwithme.runwithme.view.activity.GroupDetailActivity
import java.time.LocalDate
import java.time.LocalTime


/**
 * A simple [Fragment] subclass.
 */
class GroupsFragment : Fragment() {

    private lateinit var binding: FragmentGroupsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentGroupsBinding.inflate(layoutInflater)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        getGroupList()
        // Inflate the layout for this fragment
        return binding.root
    }

        private fun getGroupList() {

            val groupList : ArrayList<Group> = ArrayList()

            if (groupList.size > 0) {
                binding.myGroupsRecyclerView.visibility = View.VISIBLE
                binding.noGroupsAvailableTextView.visibility = View.INVISIBLE
                setupGroupsRecyclerView(groupList)
            }else{
                binding.myGroupsRecyclerView.visibility = View.INVISIBLE
                binding.noGroupsAvailableTextView.visibility = View.VISIBLE
            }
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