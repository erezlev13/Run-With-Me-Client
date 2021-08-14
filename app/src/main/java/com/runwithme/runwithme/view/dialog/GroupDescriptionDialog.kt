package com.runwithme.runwithme.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.runwithme.runwithme.databinding.DialogGroupDescriptionBinding

class GroupDescriptionDialog(private val descriptionBody: String) : DialogFragment() {

    private lateinit var binding: DialogGroupDescriptionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            binding = DialogGroupDescriptionBinding.inflate(layoutInflater)
            builder.setView(binding.root)
            binding.groupDescriptionText.text = descriptionBody
            binding.okGorupDescriptionButton.setOnClickListener {
                dialog?.cancel()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "GroupDescriptionDialog"
    }
}