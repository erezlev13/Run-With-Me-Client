package com.runwithme.runwithme.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.almatechnologies.datatracker.utils.GenericDiffUtil
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.FriendInGroupRowLayoutBinding
import com.runwithme.runwithme.databinding.FriendRowLayoutBinding
import com.runwithme.runwithme.databinding.GroupStatisticsListBinding
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ImageUtils

private const val TAG = "GroupMembersAdapter"

class GroupMembersAdapter(private var members: ArrayList<User> =  ArrayList()) : RecyclerView.Adapter<GroupMembersAdapter.GroupMembersViewHolder>() {

    init {
        Log.d(TAG, "GroupMembersAdapter called, members are: $members")
    }

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

    fun updateData(newMembers: ArrayList<User>) {
        val friendsDiffUtil = GenericDiffUtil(this.members, newMembers)
        val diffUtilResult = DiffUtil.calculateDiff(friendsDiffUtil)
        members = newMembers
        diffUtilResult.dispatchUpdatesTo(this)
    }
}