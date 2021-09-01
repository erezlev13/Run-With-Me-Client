package com.runwithme.runwithme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.databinding.StatisticsGroupRowLayoutBinding
import com.runwithme.runwithme.model.GroupRun
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.TimeUtils
import java.time.format.DateTimeFormatter


class GroupStatisticsAdapter(val pastGroupRuns: ArrayList<GroupRun> =  ArrayList()) :
    RecyclerView.Adapter<GroupStatisticsAdapter.GroupStatisticsViewHolder>() {

    private var onCompareClickListener: OnCompareClickListener? = null

    fun setOnCompareClickListener(onCompareClickListener: OnCompareClickListener) {
        this.onCompareClickListener = onCompareClickListener
    }

    /** Adapter Methods: */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupStatisticsViewHolder {
        return GroupStatisticsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GroupStatisticsViewHolder, position: Int) {
        if(onCompareClickListener != null){
            holder.bind(pastGroupRuns[position],onCompareClickListener!!)
        }

    }

    override fun getItemCount(): Int = pastGroupRuns.size



    interface OnCompareClickListener {
        fun onCompareClick(model: GroupRun)
    }

    /** View Holder: */
    class GroupStatisticsViewHolder(val binding :StatisticsGroupRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groupRun: GroupRun,onCompareClickListener: OnCompareClickListener) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = TimeUtils.stringToLocalDate(groupRun.date)
            binding.groupRunDate.text  = date.format(formatter)
            binding.avgDistanceValueTextView.text = groupRun.groupRunData.averageDistance.toString() + "KM"
            binding.avgPaceValueTextView.text = groupRun.groupRunData.averageOfAveragePace
            binding.avgStepsValueTextView.text = groupRun.groupRunData.averageSteps.toString()

            binding.compareButton.setOnClickListener {
                if (onCompareClickListener != null) {
                    onCompareClickListener!!.onCompareClick(groupRun)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): GroupStatisticsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StatisticsGroupRowLayoutBinding.inflate(layoutInflater, parent, false)
                return GroupStatisticsViewHolder(binding)
            }
        }
    }
}