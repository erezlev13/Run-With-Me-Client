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
        if(onClickListener != null){
            holder.bind(groupList[position],onClickListener!!)
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(model: Group)
    }

    class MyViewHolder(val binding: GroupRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group, onClickListener: OnClickListener) {
            binding.groupNameTextView.text = group.name
            if(group.photoUri.isNotEmpty()) {
                binding.groupImageView.setImageBitmap(ImageUtils.encodedStringToBitmap(group.photoUri))
            }
            itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(group)
                }
            }

        }
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GroupRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

}