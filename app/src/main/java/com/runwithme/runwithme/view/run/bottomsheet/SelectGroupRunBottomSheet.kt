package com.runwithme.runwithme.view.run.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.runwithme.runwithme.adapters.SelectGroupRunAdapter
import com.runwithme.runwithme.databinding.SelectGroupRunBottomSheetBinding
import com.runwithme.runwithme.model.GroupRun
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.viewmodels.GroupViewModel
import com.runwithme.runwithme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectGroupRunBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: SelectGroupRunBottomSheetBinding
    private lateinit var selectedGroupRun: GroupRun
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var userViewModel: UserViewModel

    private var onSelectGroupRunListener: OnSelectGroupRun? = null

    interface OnSelectGroupRun {
        fun selectGroupRun(group: GroupRun)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SelectGroupRunBottomSheetBinding.inflate(inflater, container, false)
        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        getGroupRunListFromDB()
        return binding.root
    }

    private fun getGroupRunListFromDB() {
        var groupRunList: ArrayList<GroupRun> = ArrayList()
        binding.selectGroupRunProgressBar.show()
        groupViewModel.getMyTodayGroupRuns()
        groupViewModel.myTodayGroupRunsResponse.observeOnce(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (response.data?.groupRuns != null) {
                        binding.selectGroupRunProgressBar.hide()
                        groupRunList = response.data.groupRuns
                        filterGroupRuns(groupRunList)
                    }
                }
            }
        })
    }

    private fun filterGroupRuns(groupRunList: ArrayList<GroupRun>) {
        var result = ArrayList<GroupRun>()
        var addGroupRun : Boolean
        userViewModel.readUser.observeOnce(this, { userList ->
            if (userList.isNotEmpty()) {
                var userRuns = userList[0].user.runs
                var runsId = createRunsId(userRuns)
                for (i in 0..groupRunList.size-1){
                    addGroupRun = true
                    for(run in groupRunList.get(i).groupRunData.membersRuns){
                        if(runsId.contains(run._id)){
                            addGroupRun = false
                            break
                        }
                    }
                    if(addGroupRun == true){
                        result.add(groupRunList.get(i))
                    }
                }
                if (result.size > 0) {
                    binding.selectGroupRunTitle.visibility = View.VISIBLE
                    binding.selectGroupRunRecycleView.visibility = View.VISIBLE
                    binding.noGroupsRunAvailableTextView.visibility = View.GONE
                    setupGroupsRecyclerView(result)
                } else {
                    binding.selectGroupRunTitle.visibility = View.GONE
                    binding.selectGroupRunRecycleView.visibility = View.INVISIBLE
                    binding.noGroupsRunAvailableTextView.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun createRunsId(userRuns: ArrayList<Run>): ArrayList<String> {
        var result = ArrayList<String>()
        for(run in userRuns){
            result.add(run._id)
        }
        return result
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
                if (onSelectGroupRunListener != null) {
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