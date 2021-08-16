package com.runwithme.runwithme.view.run.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.runwithme.runwithme.adapters.GroupsAdapter
import com.runwithme.runwithme.adapters.SelectGroupRunAdapter
import com.runwithme.runwithme.databinding.RunBottomSheetBinding
import com.runwithme.runwithme.databinding.SelectGroupRunBottomSheetBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.model.GroupRun
import com.runwithme.runwithme.utils.Constants
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.view.groups.GroupDetailActivity
import com.runwithme.runwithme.viewmodels.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectGroupRunBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: SelectGroupRunBottomSheetBinding
    private lateinit var selectedGroupRun: GroupRun
    private lateinit var groupViewModel: GroupViewModel
    private var onSelectGroupRunListener: OnSelectGroupRun? = null

    interface OnSelectGroupRun {
        fun selectGroupRun(group:GroupRun)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SelectGroupRunBottomSheetBinding.inflate(inflater, container, false)
        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
        getGroupRunListFromDB()
        return binding.root
    }

    private fun getGroupRunListFromDB() {
        var groupRunList : ArrayList<GroupRun> = ArrayList()
        binding.selectGroupRunProgressBar.show()
        groupViewModel.getMyTodayGroupRuns()
        groupViewModel.myTodayGroupRunsResponse.observeOnce(this,{response ->
            when(response){
                is NetworkResult.Success -> {
                    if(response.data?.groupRuns != null) {
                        binding.selectGroupRunProgressBar.hide()
                        groupRunList = response.data.groupRuns
                        if (groupRunList.size > 0) {
                            binding.selectGroupRunTitle.visibility = View.VISIBLE
                            binding.selectGroupRunRecycleView.visibility = View.VISIBLE
                            binding.noGroupsRunAvailableTextView.visibility = View.GONE
                            setupGroupsRecyclerView(groupRunList)
                        }else{
                            binding.selectGroupRunTitle.visibility = View.GONE
                            binding.noGroupsRunAvailableTextView.visibility = View.GONE
                            binding.selectGroupRunRecycleView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun setupGroupsRecyclerView(groupRunList: ArrayList<GroupRun>) {

        binding.selectGroupRunRecycleView.layoutManager = LinearLayoutManager(activity)
        binding.selectGroupRunRecycleView.setHasFixedSize(true)
        val selectGroupRunAdapter = SelectGroupRunAdapter(groupRunList)
        binding.selectGroupRunRecycleView.adapter = selectGroupRunAdapter

        selectGroupRunAdapter.setOnGroupRunClickListener(object :
            SelectGroupRunAdapter.OnGroupRunClickListener {
            override fun onGroupRunClick(groupRun: GroupRun) {
                selectedGroupRun = groupRun
                if(onSelectGroupRunListener != null){
                    onSelectGroupRunListener!!.selectGroupRun(selectedGroupRun)
                }

            }
        })
    }

    fun setOnSelectGroupRunListener(onClickListener: OnSelectGroupRun) {
        this.onSelectGroupRunListener = onClickListener
    }

    companion object {
        const val TAG = "SelectGroupRunBottomSheet"
    }
}