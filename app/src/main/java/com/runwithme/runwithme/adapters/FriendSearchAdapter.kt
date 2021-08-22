package com.runwithme.runwithme.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.runwithme.runwithme.utils.GenericDiffUtil
import com.runwithme.runwithme.R
import com.runwithme.runwithme.databinding.FriendRowLayoutBinding
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.ExtensionFunctions.hide
import com.runwithme.runwithme.utils.ExtensionFunctions.show
import com.runwithme.runwithme.utils.ImageUtils.encodedStringToBitmap

class FriendSearchAdapter(
    private var friendsList: ArrayList<User>
) : RecyclerView.Adapter<FriendSearchAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(onClickListener != null){
            holder.bind(friendsList[position],onClickListener!!)
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
        fun onClick(model: User)
    }

    class MyViewHolder(val binding: FriendRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: User,onClickListener: OnClickListener) {
            binding.friendNameTextView.text = friend.firstName + " " + friend.lastName
            binding.checkImageView.hide()
            binding.addFriendImageButton.show()
            if(friend.photoUri.isNotEmpty()){
                binding.friendImageView.setImageBitmap(encodedStringToBitmap(friend.photoUri))
            }else{
                binding.friendImageView.setImageResource(R.drawable.ic_account_circle)
            }
            binding.addFriendImageButton.setOnClickListener {
                if (onClickListener != null) {
                    binding.checkImageView.show()
                    binding.addFriendImageButton.hide()
                    onClickListener!!.onClick(friend)

                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FriendRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }
}