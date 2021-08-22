package com.runwithme.runwithme.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.SelectFriendRowLayoutBinding
import com.runwithme.runwithme.model.User

class SelectFriendAdapter(
    private var friendsList: ArrayList<User>
) : RecyclerView.Adapter<SelectFriendAdapter.MyViewHolder>() {

    private var onCheckListener: OnCheckListener? = null
    private var onUncheckListener: OnUncheckListener? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(onCheckListener != null && onUncheckListener != null){
            holder.bind(friendsList[position],onCheckListener!!,onUncheckListener!!)
        }


    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    fun setOnCheckListener(onCheckListener: OnCheckListener) {
        this.onCheckListener = onCheckListener
    }

    interface OnCheckListener {
        fun onCheck(model: User)
    }

    fun setOnUncheckListener(onUncheckListener: OnUncheckListener) {
        this.onUncheckListener = onUncheckListener
    }

    interface OnUncheckListener {
        fun onUncheck(model: User)
    }

    class MyViewHolder(val binding: SelectFriendRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friend: User,onCheckListener:OnCheckListener,onUncheckListener: OnUncheckListener) {
            binding.friendNameTextView.text = friend.firstName + " " + friend.lastName
            if(friend.photoUri.isNotEmpty()){
                val imgBytes: ByteArray = Base64.decode(friend.photoUri, Base64.DEFAULT);
                val bitmap : Bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                binding.friendImageView.setImageBitmap(bitmap)
            }else{
                binding.friendImageView.setImageResource(R.drawable.ic_account_circle)
            }

            binding.friendCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    if(onCheckListener != null){
                        onCheckListener!!.onCheck(friend);
                    }
                }
                else{
                    if(onUncheckListener != null){
                        onUncheckListener!!.onUncheck(friend);
                    }
                }
            }

        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SelectFriendRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }
}