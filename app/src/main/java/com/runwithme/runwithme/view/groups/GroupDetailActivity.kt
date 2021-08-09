package com.runwithme.runwithme.view.groups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.runwithme.runwithme.databinding.ActivityGroupDetailBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.utils.Constants.EXTRA_GROUP_DETAILS

class GroupDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGroupDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var groupDetail: Group? = null

        if (intent.hasExtra(EXTRA_GROUP_DETAILS)) {
            // get the Serializable data model class with the details in it
            groupDetail =
                intent.getSerializableExtra(EXTRA_GROUP_DETAILS) as Group
        }

        if(groupDetail != null){
            setSupportActionBar(binding.groupDetailToolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false);
            binding.titleToolbar.text = groupDetail!!.name
            binding.groupDetailToolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

    }
}