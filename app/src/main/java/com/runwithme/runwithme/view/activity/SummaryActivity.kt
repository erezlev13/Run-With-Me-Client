package com.runwithme.runwithme.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivitySummaryBinding
import com.runwithme.runwithme.utils.Constants.AVG_PACE
import com.runwithme.runwithme.utils.Constants.DISTANCE
import com.runwithme.runwithme.utils.Constants.TIME

class SummaryActivity : AppCompatActivity() {

    /** Properties: */
    private lateinit var binding: ActivitySummaryBinding

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply toolbar.
        setSupportActionBar(binding.summaryToolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

        getDataAndSetViews()
        onSaveClickListener()
    }

    override fun onBackPressed() {
        // Override this so the functionality won't work
        Snackbar.make(binding.saveSummaryButton, "Please save or delete this summary", Snackbar.LENGTH_LONG).show()
    }

    /** Menu Methods: */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("Summary", "onCreateOptionsMenu was called")
        menuInflater.inflate(R.menu.summary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("Summary", "onOptionsItemSelected was called")
        val isSelected: Boolean

        when (item.itemId) {
            R.id.delete_summary_menu_item -> {
                // Don't save those running details and continue to the main activity.
                Log.d("Summary", "delete menu item was clicked")
                getBackToMainActivity()
                isSelected = true
            }

            else -> isSelected = false
        }

        return isSelected
    }

    /** Class Methods: */
    private fun getDataAndSetViews() {
        binding.timeTextView.text = intent.getStringExtra(TIME)
        binding.paceTextView.text = intent.getStringExtra(AVG_PACE)
        binding.distanceSummaryTextView.text = intent.getStringExtra(DISTANCE)
    }

    private fun onSaveClickListener() {
        binding.saveSummaryButton.setOnClickListener {
            // TODO: Save the data of the current running on server.
            getBackToMainActivity()
        }
    }

    private fun getBackToMainActivity() {
        Log.d("Summary", "getBackToMainActivity was called")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}