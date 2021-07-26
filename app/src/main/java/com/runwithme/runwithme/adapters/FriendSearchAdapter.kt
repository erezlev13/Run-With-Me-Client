package com.runwithme.runwithme.adapters


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.almatechnologies.datatracker.utils.GenericDiffUtil
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.FriendRowLayoutBinding
import com.runwithme.runwithme.model.User

class FriendSearchAdapter(
    private var friendsList: ArrayList<User>
) : RecyclerView.Adapter<FriendSearchAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = friendsList[position]

        if (holder is MyViewHolder) {
            holder.binding.friendNameTextView.text = friend.email
            if(friend.photoUri.isNotEmpty()){
                val imgBytes: ByteArray = Base64.decode(friend.photoUri, Base64.DEFAULT);
                val bitmap : Bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                holder.binding.friendImageView.setImageBitmap(bitmap)
            }else{
                holder.binding.friendImageView.setImageResource(R.drawable.ic_account_circle)
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