package com.runwithme.runwithme.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.R
import com.runwithme.runwithme.utils.ImageUtils

class GroupStatisticsAdapter(private val allRunData: ArrayList<HashMap<String, String>> =  ArrayList()) :
    RecyclerView.Adapter<GroupStatisticsAdapter.GroupStatisticsViewHolder>() {

    /** View Holder: */
    class GroupStatisticsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val avgDistance: TextView = view.findViewById(R.id.avg_distance_value_text_view)
        private val avgSteps: TextView = view.findViewById(R.id.avg_steps_value_text_view)
        private val avgPace: TextView = view.findViewById(R.id.avg_pace_value_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(runData: HashMap<String, String>) {
            avgDistance.text = runData["Distance"]
            avgSteps.text = runData["Steps"]
            avgPace.text = runData["Pace"]
        }
    }

    /** Adapter Methods: */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GroupStatisticsViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.friend_in_group_row_layout, viewGroup, false)

        return GroupStatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupStatisticsViewHolder, position: Int) {
        holder.bind(allRunData[position])
    }

    override fun getItemCount(): Int = allRunData.size
}