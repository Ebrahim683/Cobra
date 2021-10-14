package com.rexoit.cobra.ui.block.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rexoit.cobra.R
import com.rexoit.cobra.data.model.blocklist.Number
import kotlinx.android.synthetic.main.single_row_call_list.view.*

class BlockListAdapter : ListAdapter<Number, BlockListAdapter.BlockListHolder>(DIFF_UTIL_CALLBACK) {

    class BlockListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(number: Number) {
            itemView.sample_number_text.text = number.phoneNumber
            itemView.sample_name_text.text = "Unknown Caller"
            itemView.time.text = number.updatedAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockListHolder {
        return BlockListHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.single_row_call_list, parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: BlockListHolder, position: Int) {
        val blockedNumber = getItem(position)
        holder.bindView(blockedNumber)
    }
}

val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Number>() {
    override fun areItemsTheSame(oldItem: Number, newItem: Number): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Number, newItem: Number): Boolean {
        return oldItem.id == newItem.id && oldItem.createdAt == newItem.createdAt
                && oldItem.phoneNumber == newItem.phoneNumber
                && oldItem.userId == newItem.userId
                && oldItem.createdAt == newItem.createdAt
    }

}