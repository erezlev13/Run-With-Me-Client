package com.runwithme.runwithme.view.groups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.runwithme.runwithme.R
import com.runwithme.runwithme.adapters.GroupMembersAdapter
import com.runwithme.runwithme.databinding.ActivityGroupDetailBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.utils.Constants.EXTRA_GROUP_DETAILS

class GroupDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGroupDetailBinding
    private lateinit var mMembersAdapter: GroupMembersAdapter

    private lateinit var avgDistance: String
    private lateinit var avgSteps: String
    private lateinit var avgPace: String
    private var runDataList = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply toolbar.
        setSupportActionBar(binding.groupDetailsToolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)     // Add back button.
        }

        var groupDetails: Group? = null

        if (intent.hasExtra(EXTRA_GROUP_DETAILS)) {
            // get the Serializable data model class with the details in it
            groupDetails =
                intent.getSerializableExtra(EXTRA_GROUP_DETAILS) as Group
        }

        if(groupDetails != null){
//            setSupportActionBar(binding.groupDetailsToolbar)
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//            supportActionBar!!.setDisplayShowTitleEnabled(false);
            binding.groupName.text = groupDetails!!.name
            showFriendList(groupDetails)
            showStatistics(groupDetails)
            showAndSchedulFutureRuns(groupDetails)
//            binding.groupDetailToolbar.setNavigationOnClickListener {
//                onBackPressed()
//            }
        }
    }

    private fun showFriendList(groupDetails: Group) {
        mMembersAdapter = GroupMembersAdapter(groupDetails.groupMembers)
    }

    private fun showStatistics(groupDetails: Group) {
        calculateAverages(groupDetails)
    }

    private fun calculateAverages(groupDetails: Group) {
        var sumDistance = 0.0
        var avgDistance = 0.0
        var sumSteps = 0.0
        var avgSteps = 0.0

//        groupDetails.groupRuns.forEach {
//            sumDistance = 0.0
//            avgDistance = 0.0
//            sumSteps = 0.0
//            avgSteps = 0.0
//            it.runners.forEach {
//                sumDistance += it.runData.distance
//                sumSteps += it.runData.steps
//            }
//        }

        groupDetails.groupMembers.forEach {
            sumDistance = 0.0
            avgDistance = 0.0
            sumSteps = 0.0
            avgSteps = 0.0
            it.runs.forEach {
                sumDistance += it.runData.distance
                sumSteps += it.runData.steps
            }

            avgDistance = sumDistance / groupDetails.groupMembers.size
            avgSteps = sumSteps / groupDetails.groupMembers.size
            val runDataMap = HashMap<String, String>()
            runDataMap["Distance"] = avgDistance.toString()
            runDataMap["Steps"] = avgSteps.toString()
            runDataList.add(runDataMap)
        }
    }

    private fun showAndSchedulFutureRuns(groupDetails: Group) {
        val intent = Intent(this, ScheduleRunActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.groups_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val isSelected: Boolean

        when (item.itemId) {
            R.id.group_description -> {
                showDescription()
                isSelected = true
            }

            android.R.id.home -> {
                onBackPressed()
                isSelected = true
            }

            else -> isSelected = false
        }

        return isSelected
    }

    private fun showDescription() {
        // TODO: show group's description in a dialog.
    }
}