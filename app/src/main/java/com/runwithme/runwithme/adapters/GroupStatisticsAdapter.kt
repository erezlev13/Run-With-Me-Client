package com.runwithme.runwithme.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ScheduledRunRowLayoutBinding
import com.runwithme.runwithme.databinding.StatisticsGroupRowLayoutBinding
import com.runwithme.runwithme.model.GroupRun
import com.runwithme.runwithme.utils.ImageUtils
import com.runwithme.runwithme.utils.TimeUtils
import java.time.format.DateTimeFormatter

class GroupStatisticsAdapter(val pastGroupRuns: ArrayList<GroupRun> =  ArrayList()) :
    RecyclerView.Adapter<GroupStatisticsAdapter.GroupStatisticsViewHolder>() {

    /** Adapter Methods: */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupStatisticsViewHolder {
        return GroupStatisticsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GroupStatisticsViewHolder, position: Int) {
        holder.bind(pastGroupRuns[position])
    }

    override fun getItemCount(): Int = pastGroupRuns.size

    /** View Holder: */
    class GroupStatisticsViewHolder(val binding :StatisticsGroupRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groupRun: GroupRun) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = TimeUtils.stringToLocalDate(groupRun.date)
            binding.groupRunDate.text  = date.format(formatter)
            binding.avgDistanceValueTextView.text = groupRun.groupRunData.averageDistance.toString() + "KM"
            binding.avgPaceValueTextView.text = groupRun.groupRunData.averageOfAveragePace
            binding.avgStepsValueTextView.text = groupRun.groupRunData.averageSteps.toString()
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