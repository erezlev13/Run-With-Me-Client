package com.runwithme.runwithme.view.groups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.runwithme.runwithme.R
import com.runwithme.runwithme.adapters.GroupMembersAdapter
import com.runwithme.runwithme.adapters.GroupStatisticsAdapter
import com.runwithme.runwithme.adapters.ScheduledRunsAdapter
import com.runwithme.runwithme.databinding.ActivityGroupDetailBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.utils.Constants.EXTRA_GROUP_DETAILS
import com.runwithme.runwithme.utils.Constants.GROUP_ID
import com.runwithme.runwithme.utils.ImageUtils
import com.runwithme.runwithme.view.dialog.GroupDescriptionDialog

private const val TAG = "GroupDetailActivity"

class GroupDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGroupDetailBinding
    private lateinit var mMembersAdapter: GroupMembersAdapter
    private lateinit var mStatisticsAdapter: GroupStatisticsAdapter
    private lateinit var mScheduleRunsAdapter: ScheduledRunsAdapter

    private var mGroupDetails: Group? = null

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

        if (intent.hasExtra(EXTRA_GROUP_DETAILS)) {
            // get the Serializable data model class with the details in it
            mGroupDetails =
                intent.getSerializableExtra(EXTRA_GROUP_DETAILS) as Group
        }

        if(mGroupDetails != null){
            binding.groupNameTitleToolbar.text = mGroupDetails!!.name
            setGroupImage(mGroupDetails!!)
            showFriendList(mGroupDetails!!)
            showStatistics(mGroupDetails!!)
            showAndScheduleFutureRuns(mGroupDetails!!)
        }
    }

    private fun setGroupImage(groupDetails: Group) {
        if (groupDetails.photoUri.isNotEmpty()) {
            binding.groupImageView.setImageBitmap(ImageUtils.encodedStringToBitmap(groupDetails.photoUri))
        }
    }

    private fun showFriendList(groupDetails: Group) {
        binding.friendsListInclude.friendsListRecyclerView
        binding.friendsListInclude.friendsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.friendsListInclude.friendsListRecyclerView.setHasFixedSize(true)
        mMembersAdapter = GroupMembersAdapter(groupDetails.groupMembers)
        binding.friendsListInclude.friendsListRecyclerView.adapter = mMembersAdapter
    }

    private fun showStatistics(groupDetails: Group) {
//        mBindingStatistics.statisticeRecyclerView.layoutManager = LinearLayoutManager(this)
//        mBindingStatistics.statisticeRecyclerView.setHasFixedSize(true)
//        mStatisticsAdapter = GroupStatisticsAdapter()
//        mBindingStatistics.statisticeRecyclerView.adapter = mStatisticsAdapter
    }

    private fun showAndScheduleFutureRuns(groupDetails: Group) {
        binding.scheduledRunsInclude.scheduledRunsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.scheduledRunsInclude.scheduledRunsRecyclerView.setHasFixedSize(true)
        mScheduleRunsAdapter = ScheduledRunsAdapter(groupDetails.groupRuns)
        binding.scheduledRunsInclude.scheduledRunsRecyclerView.adapter = mScheduleRunsAdapter
        if(groupDetails.groupRuns.size == 0){
            binding.scheduledRunsInclude.scheduledRunsRecyclerView.visibility = View.GONE
            binding.scheduledRunsInclude.noFutureRunsAvailableTextView.visibility = View.VISIBLE
        }
        else{
            binding.scheduledRunsInclude.scheduledRunsRecyclerView.visibility = View.VISIBLE
            binding.scheduledRunsInclude.noFutureRunsAvailableTextView.visibility = View.GONE
        }
        binding.scheduledRunsInclude.scheduledARunButton.setOnClickListener {
            val intent = Intent(this, ScheduleRunActivity::class.java)
            intent.putExtra(EXTRA_GROUP_DETAILS, mGroupDetails)
            intent.putExtra(GROUP_ID, groupDetails._id)
            startActivity(intent)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.groups_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val isSelected: Boolean

        when (item.itemId) {
            R.id.group_description -> {
                showDescription(mGroupDetails)
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

    private fun showDescription(groupDetails: Group?) {
        if (groupDetails != null) {
            val dialog = GroupDescriptionDialog(groupDetails.description)
            dialog.show(supportFragmentManager, GroupDescriptionDialog.TAG)
        }
    }
}