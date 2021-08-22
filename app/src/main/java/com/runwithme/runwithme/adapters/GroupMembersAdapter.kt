package com.runwithme.runwithme.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.databinding.FriendInGroupRowLayoutBinding
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ImageUtils


class GroupMembersAdapter(private var members: ArrayList<User> =  ArrayList()) : RecyclerView.Adapter<GroupMembersAdapter.GroupMembersViewHolder>() {


    /** View Holder: */
    class GroupMembersViewHolder(val binding: FriendInGroupRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(member: User) {
            binding.friendInGroupNameTextView.text = "${member.firstName} ${member.lastName}"
            if (member.photoUri.isNotEmpty()) {
                binding.friendInGroupImageView.setImageBitmap(ImageUtils.encodedStringToBitmap(member.photoUri))
            }
        }

        companion object {
            fun from(parent: ViewGroup): GroupMembersViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FriendInGroupRowLayoutBinding.inflate(layoutInflater, parent, false)
                return GroupMembersViewHolder(binding)
            }
        }
    }

    /** Adapter Methods: */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GroupMembersViewHolder {
        return GroupMembersViewHolder.from(viewGroup)
    }

    override fun onBindViewHolder(holder: GroupMembersViewHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount(): Int {
        return members.size
    }

}