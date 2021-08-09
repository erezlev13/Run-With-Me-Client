package com.runwithme.runwithme.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.GroupRowLayoutBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.utils.ImageUtils

class GroupsAdapter(
    private var groupList: ArrayList<Group>
) : RecyclerView.Adapter<GroupsAdapter.MyViewHolder>()  {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val group = groupList[position]

        holder.binding.groupNameTextView.text = group.name
        if(group.photoUri.isNotEmpty()) {
            holder.binding.groupImageView.setImageBitmap(ImageUtils.encodedStringToBitmap(group.photoUri))
        }


        holder.itemView.setOnClickListener {

            if (onClickListener != null) {
                onClickListener!!.onClick(position, group)
            }
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Group)
    }

    class MyViewHolder(val binding: GroupRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GroupRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

}