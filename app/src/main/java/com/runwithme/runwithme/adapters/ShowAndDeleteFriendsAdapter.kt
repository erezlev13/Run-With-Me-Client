package com.runwithme.runwithme.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.ShowAndDeleteFriendRowLayoutBinding
import com.runwithme.runwithme.model.User

class ShowAndDeleteFriendsAdapter(
    private var friendsList: ArrayList<User>
) : RecyclerView.Adapter<ShowAndDeleteFriendsAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = friendsList[position]

        holder.binding.friendNameTextView.text = friend.firstName + " " + friend.lastName
        if(friend.photoUri.isNotEmpty()){
            val imgBytes: ByteArray = Base64.decode(friend.photoUri, Base64.DEFAULT);
            val bitmap : Bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
            holder.binding.friendImageView.setImageBitmap(bitmap)
        }else{
            holder.binding.friendImageView.setImageResource(R.drawable.ic_account_circle)
        }

        holder.binding.deleteFriendImageButton.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, friend)
                Snackbar.make(holder.binding.friendCardView,
                    "${friendsList[position].firstName}" +
                            " ${friendsList[position].lastName}" +
                            " is deleted from your friend list", Snackbar.LENGTH_LONG).show()
                friendsList.removeAt(position)
                notifyDataSetChanged()

            }
        }
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: User)
    }

    class MyViewHolder(val binding: ShowAndDeleteFriendRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShowAndDeleteFriendRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }
}