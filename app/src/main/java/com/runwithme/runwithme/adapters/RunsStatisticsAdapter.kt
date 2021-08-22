package com.runwithme.runwithme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.RunStatRowLayoutBinding
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.utils.TimeUtils
import java.time.format.DateTimeFormatter


class RunsStatisticsAdapter(
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
        if(listener != null){
            holder.bind(runsList[position],listener!!)
        }
    }

    class MyViewHolder(val binding: RunStatRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(run: Run,listener:OnRunDetailsClick) {
            binding.runTypeTitle.text = run.runType + " Run"
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = TimeUtils.stringToLocalDate(run.date)
            binding.runDate.text = date.format(formatter)
            binding.totalTimeTextView.text = TimeUtils.calculateTimeDifference(
                run.startTime,run.endTime) + "H"
            binding.totalStepsTextView.text = run.runData.steps.toString()
            binding.distanceTextView.text = run.runData.distance.toString()+ "KM"
            binding.avgPaceTextView.text = run.runData.averagePace

            itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onRunDetailsClick(run)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RunStatRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }
}