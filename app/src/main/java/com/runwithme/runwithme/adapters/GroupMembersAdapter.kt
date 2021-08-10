package com.runwithme.runwithme.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.R
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ImageUtils

class GroupMembersAdapter(private val members: ArrayList<User> =  ArrayList()) : RecyclerView.Adapter<GroupMembersAdapter.GroupMembersViewHolder>() {

    /** View Holder: */
    class GroupMembersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val memberName: TextView = view.findViewById(R.id.friend_in_group__name_textView)
        private val memberImage: ImageView = view.findViewById(R.id.friend_in_group_imageView)

        @SuppressLint("SetTextI18n")
        fun bind(member: User) {
            memberName.text = "$member.firstName $member.lastName"
            memberImage.setImageBitmap(ImageUtils.encodedStringToBitmap(member.photoUri))
        }
    }

    /** Adapter Methods: */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GroupMembersViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.friend_in_group_row_layout, viewGroup, false)

        return GroupMembersViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupMembersViewHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount(): Int = members.size
}