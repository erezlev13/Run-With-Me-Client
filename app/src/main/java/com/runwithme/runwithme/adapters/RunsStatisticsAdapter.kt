package com.runwithme.runwithme.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.RunStatRowLayoutBinding
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.RunType
import com.runwithme.runwithme.utils.TimeUtils
import com.runwithme.runwithme.view.profile.StatisticsDetailsActivity
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class RunsStatisticsAdapter(private val context: Context,
                           private var runsList: ArrayList<Run>
) : RecyclerView.Adapter<RunsStatisticsAdapter.MyViewHolder>()  {

    var listener: OnRunDetailsClick? = null

    interface OnRunDetailsClick {
        fun onRunDetailsClick(run: Run)
    }

    override fun getItemCount(): Int {
        return runsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val run = runsList[position]

        holder.binding.runTypeTitle.text = run.runType + " Run"
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = TimeUtils.stringToLocalDate(run.date)
        holder.binding.runDate.text = date.format(formatter)
        holder.binding.totalTimeTextView.text = TimeUtils.calculateTimeDifference(
            run.startTime,run.endTime) + "H"
        holder.binding.totalStepsTextView.text = run.runData.steps.toString()
        holder.binding.distanceTextView.text = run.runData.distance.toString()+ "KM"
        holder.binding.avgPaceTextView.text = run.runData.averagePace

        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onRunDetailsClick(run)
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