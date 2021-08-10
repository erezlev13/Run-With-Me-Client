package com.runwithme.runwithme.view.groups

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivityScheduleRunBinding

class ScheduleRunActivity : AppCompatActivity() {

    /** Properties: */
    private lateinit var binding: ActivityScheduleRunBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_run)
        binding = ActivityScheduleRunBinding.inflate(layoutInflater)

        onLocationWriteUpdate()
        onDateClickListener()
    }

    private fun onDateClickListener() {
        binding.dateTextInputEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        binding.dateTextInputEditText.doOnTextChanged { text, start, before, count ->
            if(binding.dateTextInputEditText.error == getString(R.string.email_empty_error)){
                binding.dateTextInputEditText.error = null
            }
        }
    }

    private fun onLocationWriteUpdate() {
        TODO("Not yet implemented")
    }
}