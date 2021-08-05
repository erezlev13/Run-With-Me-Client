package com.runwithme.runwithme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.RunStatRowLayoutBinding
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.RunType
import com.runwithme.runwithme.utils.TimeUtils
import java.time.format.DateTimeFormatter


class RunsStatisticsAdapter(private val context: Context,
                           private var runsList: ArrayList<Run>
) : RecyclerView.Adapter<RunsStatisticsAdapter.MyViewHolder>()  {

    override fun getItemCount(): Int {
        return runsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val run = runsList[position]

        if (holder is MyViewHolder) {
            holder.binding.runTypeTitle.text = RunType.describe(run.runType) + " Run"
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            holder.binding.runDate.text = run.date.format(formatter)
            holder.binding.totalTimeTextView.text = TimeUtils.calculateTimeDifference(
                run.startTime,run.endTime) + "H"
            holder.binding.totalStepsTextView.text = run.runData.steps.toString()
            holder.binding.distanceTextView.text = run.runData.distance.toString()+ "KM"

            holder.itemView.setOnClickListener {
                // TODO: send user to statistics details activity.
            }
        }
    }
    class MyViewHolder(val binding: RunStatRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RunStatRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }
}