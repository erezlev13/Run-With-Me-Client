package com.runwithme.runwithme.view.run.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.runwithme.runwithme.databinding.RunBottomSheetBinding

class RunBottomSheet(val listener: OnContinueStopClick) : BottomSheetDialogFragment() {

    /** Properties: */
    private var _binding: RunBottomSheetBinding? = null
    private val binding: RunBottomSheetBinding get() = _binding!!

    /** Interfaces: */
    interface OnContinueStopClick {
        fun onContinueClick()
        fun onStopClick()
    }

    init {
        isCancelable = false
    }

    /** Fragment Methods: */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RunBottomSheetBinding.inflate(inflater, container, false)

        onContinueClickListener()
        onStopClickListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Class Methods: */
    private fun onContinueClickListener() {
        binding.continueRunButton.setOnClickListener {
            listener.onContinueClick()
            this.dismiss()
        }
    }

    private fun onStopClickListener() {
        binding.stopRunButton.setOnClickListener {
            listener.onStopClick()
        }
    }

    companion object {
        const val TAG = "RunBottomSheet"
    }
}