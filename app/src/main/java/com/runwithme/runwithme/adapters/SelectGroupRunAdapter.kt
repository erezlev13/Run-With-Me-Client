package com.runwithme.runwithme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.SelectGroupRunRowLayoutBinding
import com.runwithme.runwithme.model.GroupRun
import com.runwithme.runwithme.utils.ImageUtils
import com.runwithme.runwithme.utils.TimeUtils

class SelectGroupRunAdapter( private var groupRunList: ArrayList<GroupRun>
) : RecyclerView.Adapter<SelectGroupRunAdapter.SelectGroupRunViewHolder>() {

    private var onGroupRunClickListener: OnGroupRunClickListener? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectGroupRunViewHolder {
        return SelectGroupRunViewHolder.from(parent)
    }

    fun setOnGroupRunClickListener(onGroupRunClickListener: OnGroupRunClickListener) {
        this.onGroupRunClickListener = onGroupRunClickListener
    }

    interface OnGroupRunClickListener {
        fun onGroupRunClick(groupRun: GroupRun)
    }


    override fun getItemCount(): Int {
        return groupRunList.size
    }

    override fun onBindViewHolder(holder: SelectGroupRunViewHolder, position: Int) {
        if(onGroupRunClickListener != null){
            holder.bind(groupRunList[position],onGroupRunClickListener!!)
        }

    }

    class SelectGroupRunViewHolder(val binding: SelectGroupRunRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(groupRun: GroupRun,onGroupRunClickListener: OnGroupRunClickListener) {
            binding.selectGroupRunGroupNameTextView.text = groupRun.group.name
            binding.selectGroupRunTimeTextView.text = "At ${TimeUtils.dateStringToTimeString(groupRun.date)}, ${groupRun.location}"
            if(groupRun.group.photoUri.isNotEmpty()) {
                binding.selectGroupRunGroupImageView.setImageBitmap(ImageUtils.encodedStringToBitmap(groupRun.group.photoUri))
            }

            itemView.setOnClickListener {
                if (onGroupRunClickListener != null) {
                    onGroupRunClickListener!!.onGroupRunClick(groupRun)
                }
            }

        }

        companion object {
            fun from(parent: ViewGroup): SelectGroupRunViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SelectGroupRunRowLayoutBinding.inflate(layoutInflater, parent, false)
                return SelectGroupRunViewHolder(binding)
            }
        }
    }
}