package com.runwithme.runwithme.view.groups

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ActivityScheduleRunBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.model.network.ScheduleRunRequest
import com.runwithme.runwithme.utils.Constants.EXTRA_GROUP_DETAILS
import com.runwithme.runwithme.utils.Constants.GROUP_ID
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.viewmodels.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ScheduleRunActivity : AppCompatActivity() {

    /** Properties: */
    private lateinit var mViewModel: GroupViewModel
    private lateinit var binding: ActivityScheduleRunBinding
    private var mGroupDetails: Group? = null
    private var groupId: String = ""
    private var location = ""
    private var date = ""
    private var time = ""
    private var calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    private var hour = ""
    private var minute = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleRunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
        if (intent.hasExtra(EXTRA_GROUP_DETAILS)) {
            // get the Serializable data model class with the details in it
            mGroupDetails =
                intent.getSerializableExtra(EXTRA_GROUP_DETAILS) as Group
        }
        groupId = intent.getStringExtra(GROUP_ID) ?: ""

        onLocationWriteUpdate()
        onDateClickListener()
        onTimeClickListener()
        onSaveClickListener()
    }

    private fun onSaveClickListener() {
        var isLocationEmpty = false
        var isDateEmpty = false
        var isTimeEmpty = false
        binding.saveScheudleButton.setOnClickListener {
            if (binding.locationTextInputEditText.text!!.isEmpty()) {
                showErrorInEditText(binding.locationTextInputEditText, getString(R.string.location_empty_error))
                isLocationEmpty = true
            }

            if (binding.dateTextInputEditText.text!!.isEmpty()) {
                showErrorInEditText(binding.dateTextInputEditText, getString(R.string.date_empty_error))
                isDateEmpty = true
            }

            if (binding.timeTextInputEditText.text!!.isEmpty()) {
                showErrorInEditText(binding.timeTextInputEditText, getString(R.string.time_empty_error))
                isTimeEmpty = true
            }

            if (!isLocationEmpty && !isDateEmpty && !isTimeEmpty) {
                // Send data to the server and move to the group details activity, with the new data updated.
                location = binding.locationTextInputEditText.text.toString()
                val scheduleRunRequest = ScheduleRunRequest(groupId, location, date, time)
                mViewModel.saveScheduleRun(scheduleRunRequest)
                mViewModel.scheduleRun.observeOnce(this,  { response ->
                    if (response.data?.groupRun != null) {
                        // Move to the next screen.
                        mGroupDetails!!.groupRuns.add(response.data!!.groupRun)
                        val intent = Intent(this, GroupDetailActivity::class.java)
                        intent.putExtra(EXTRA_GROUP_DETAILS, mGroupDetails)
                        startActivity(intent)
                    } else {
                        Snackbar.make(binding.root, "Something went wrong, please try again", Snackbar.LENGTH_LONG)
                    }
                })
            }
        }
    }

    private fun onLocationWriteUpdate() {
        binding.locationTextInputEditText.doOnTextChanged { _, _, _, _ ->
            if(binding.locationTextInputEditText.error == getString(R.string.location_empty_error)){
                binding.locationTextInputEditText.error = null
            }
        }
    }

    private fun onDateClickListener() {
            binding.dateTextInputEditText.setOnClickListener {
                showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
        datePicker.show(supportFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            setDate(calendar)
            showDateOnEditText(date)
        }

        datePicker.addOnNegativeButtonClickListener {
            showErrorInEditText(binding.dateTextInputEditText, getString(R.string.date_empty_error))
        }

        datePicker.addOnDismissListener {
            showErrorInEditText(binding.dateTextInputEditText, getString(R.string.date_empty_error))
        }
    }

    private fun setDate(calendar: Calendar) {
        date = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) +1}/${calendar.get(Calendar.YEAR)}"
    }

    private fun showDateOnEditText(date: String) {
        binding.dateTextInputEditText.setText(date)
    }

    private fun onTimeClickListener() {
        binding.timeTextInputEditText.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val timePicker =
                MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select Run time")
                        .build()
        timePicker.show(supportFragmentManager, "Time Picker")

        timePicker.addOnPositiveButtonClickListener {
            hour = timePicker.hour.toString()
            minute = timePicker.minute.toString()
            setTime(hour, minute)
            showTimeOnEditText(time)
        }

        timePicker.addOnNegativeButtonClickListener {
            showErrorInEditText(binding.timeTextInputEditText, getString(R.string.time_empty_error))
        }

        timePicker.addOnDismissListener {
            showErrorInEditText(binding.timeTextInputEditText, getString(R.string.time_empty_error))
        }
    }

    private fun setTime(hour: String, minute: String) {
        time = "$hour:$minute"
    }

    private fun showTimeOnEditText(time: String) {
        binding.timeTextInputEditText.setText(time)
    }

    private fun showErrorInEditText(editText: TextInputEditText, errorMsg: String) {
        if (editText.text.isNullOrEmpty()) {
            editText.error = errorMsg
        } else {
            editText.error = null
        }
    }
}