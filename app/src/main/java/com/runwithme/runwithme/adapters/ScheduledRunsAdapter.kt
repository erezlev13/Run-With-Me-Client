package com.runwithme.runwithme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.ScheduledRunsListBinding
import com.runwithme.runwithme.model.GroupRun

class ScheduledRunsAdapter(private var scheduledRuns: ArrayList<GroupRun> = ArrayList()) :
        RecyclerView.Adapter<ScheduledRunsAdapter.ScheduledRunsViewHolder>() {
    class ScheduledRunsViewHolder(binding: ScheduledRunsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(run: GroupRun) {

        }

        companion object {
            fun from(parent: ViewGroup): ScheduledRunsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ScheduledRunsListBinding.inflate(layoutInflater, parent, false)
                return ScheduledRunsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledRunsViewHolder {
        return ScheduledRunsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScheduledRunsViewHolder, position: Int) {
        holder.bind(scheduledRuns[position])
    }

    override fun getItemCount(): Int = scheduledRuns.size
}