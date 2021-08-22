package com.runwithme.runwithme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.ScheduledRunRowLayoutBinding
import com.runwithme.runwithme.databinding.ScheduledRunsListBinding
import com.runwithme.runwithme.model.GroupRun
import com.runwithme.runwithme.utils.TimeUtils
import java.time.format.DateTimeFormatter

class ScheduledRunsAdapter(private var scheduledRuns: ArrayList<GroupRun> = ArrayList()) :
        RecyclerView.Adapter<ScheduledRunsAdapter.ScheduledRunsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledRunsViewHolder {
        return ScheduledRunsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScheduledRunsViewHolder, position: Int) {
        holder.bind(scheduledRuns[position])
    }

    override fun getItemCount(): Int = scheduledRuns.size

    class ScheduledRunsViewHolder(val binding: ScheduledRunRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(run: GroupRun) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = TimeUtils.stringToLocalDate(run.date)
            binding.scheduledRunDateValueTextView.text  = date.format(formatter)+ ", At ${TimeUtils.dateStringToTimeString(run.date)}"
            binding.scheduledRunLocationValueTextView.text = run.location

        }

        companion object {
            fun from(parent: ViewGroup): ScheduledRunsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ScheduledRunRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ScheduledRunsViewHolder(binding)
            }
        }
    }
}