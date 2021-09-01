package com.runwithme.runwithme.view.groups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.runwithme.runwithme.databinding.ActivityCompareStatisticsBinding
import com.runwithme.runwithme.databinding.ActivityGroupDetailBinding

class CompareStatisticsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCompareStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.compareStatisticsToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        binding.compareStatisticsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}