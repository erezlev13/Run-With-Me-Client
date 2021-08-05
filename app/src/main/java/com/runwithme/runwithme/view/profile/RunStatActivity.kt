package com.runwithme.runwithme.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.runwithme.runwithme.adapters.RunsStatisticsAdapter
import com.runwithme.runwithme.databinding.ActivityRunStatBinding
import com.runwithme.runwithme.model.Run

class RunStatActivity : AppCompatActivity(), RunsStatisticsAdapter.OnRunDetailsClick {

    private lateinit var binding: ActivityRunStatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunStatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.runsStatToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.runsStatToolbar.setNavigationOnClickListener {
            onBackPressed()
        }


    }

//    private fun getRunStatisticsList() {
//
//        val runStatisticsList : ArrayList<Run>
//
//        if (runStatisticsList.size > 0) {
//            binding.runStatList.visibility = View.VISIBLE
//            binding.noRunsAvailableTextView.visibility = View.INVISIBLE
//            setupRunStatisticsRecyclerView(runStatisticsList)
//        }else{
//            binding.runStatList.visibility = View.INVISIBLE
//            binding.noRunsAvailableTextView.visibility = View.VISIBLE
//        }
//    }

    private fun setupRunStatisticsRecyclerView(runStatisticsList: ArrayList<Run>) {

        binding.runStatList.layoutManager = LinearLayoutManager(this)
        binding.runStatList.setHasFixedSize(true)
        val runsStatisticsAdapter = RunsStatisticsAdapter(this, runStatisticsList)
        runsStatisticsAdapter.listener = this
        binding.runStatList.adapter = runsStatisticsAdapter

    }

    override fun onRunDetailsClick(run: Run) {
        // TODO: send user to statistics details activity.
    }
}