package com.runwithme.runwithme.adapters


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.almatechnologies.datatracker.utils.GenericDiffUtil
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.FriendRowLayoutBinding
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.show

class FriendSearchAdapter(
    private var friendsList: ArrayList<User>
) : RecyclerView.Adapter<FriendSearchAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = friendsList[position]

        if (holder is MyViewHolder) {
            holder.binding.friendNameTextView.text = friend.email
            holder.binding.checkImageView.hide()
            holder.binding.addFriendImageButton.show()
            if(friend.photoUri.isNotEmpty()){
                val imgBytes: ByteArray = Base64.decode(friend.photoUri, Base64.DEFAULT);
                val bitmap : Bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                holder.binding.friendImageView.setImageBitmap(bitmap)
            }else{
                holder.binding.friendImageView.setImageResource(R.drawable.ic_account_circle)
            }
        }
        holder.binding.addFriendImageButton.setOnClickListener {
            if (onClickListener != null) {
                holder.binding.checkImageView.show()
                holder.binding.addFriendImageButton.hide()
                onClickListener!!.onClick(position, friend)

            }
        }
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    fun filterList(filteredList: ArrayList<User>) {
        val friendsDiffUtil = GenericDiffUtil(this.friendsList,filteredList)
        val diffUtilResult = DiffUtil.calculateDiff(friendsDiffUtil)
        friendsList = filteredList
        diffUtilResult.dispatchUpdatesTo(this)

    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: User)
    }

    class MyViewHolder(val binding: FriendRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FriendRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }
}