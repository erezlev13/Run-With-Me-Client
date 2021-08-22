package com.runwithme.runwithme.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.runwithme.runwithme.adapters.RunsStatisticsAdapter
import com.runwithme.runwithme.databinding.ActivityRunStatBinding
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.utils.Constants
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.NetworkResult
import com.runwithme.runwithme.viewmodels.RunViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunStatActivity : AppCompatActivity(), RunsStatisticsAdapter.OnRunDetailsClick {

    private lateinit var binding: ActivityRunStatBinding
    private lateinit var mRunViewModel: RunViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunStatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.runStatisticsToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false);

        binding.runStatisticsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        mRunViewModel = ViewModelProvider(this).get(RunViewModel::class.java)

        getRunListFromDB()

    }

    private fun getRunListFromDB() {
        var runList: ArrayList<Run> = ArrayList()
        binding.runStatProgressBar.show()
        mRunViewModel.getMyRuns()
        mRunViewModel.myRunsResponse.observeOnce(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (response.data!!.runs != null) {
                        binding.runStatProgressBar.hide()
                        runList = response.data.runs
                        if (runList.size > 0) {
                            binding.myRunStatRecycleView.visibility = View.VISIBLE
                            binding.noRunsAvailableTextView.visibility = View.INVISIBLE
                            setupRunStatisticsRecyclerView(runList)
                        } else {
                            binding.myRunStatRecycleView.visibility = View.INVISIBLE
                            binding.noRunsAvailableTextView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun setupRunStatisticsRecyclerView(runStatisticsList: ArrayList<Run>) {

        binding.myRunStatRecycleView.layoutManager = LinearLayoutManager(this)
        binding.myRunStatRecycleView.setHasFixedSize(true)
        val runsStatisticsAdapter = RunsStatisticsAdapter(runStatisticsList)
        runsStatisticsAdapter.listener = this
        binding.myRunStatRecycleView.adapter = runsStatisticsAdapter

    }

    override fun onRunDetailsClick(run: Run) {
        val intent = Intent(this, StatisticsDetailsActivity::class.java)
        intent.putExtra(
            Constants.EXTRA_RUN_DETAILS,
            run
        ) // Passing the complete serializable data class to the detail activity using intent.
        startActivity(intent)
    }
}