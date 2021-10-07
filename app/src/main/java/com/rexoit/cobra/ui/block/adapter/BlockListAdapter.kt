package com.rexoit.cobra.ui.block.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rexoit.cobra.R
import com.rexoit.cobra.data.model.user.BlockedNumber

class BlockListAdapter(
    private val context: Context,
    private var arrayList: ArrayList<BlockedNumber>
) :
    RecyclerView.Adapter<BlockListAdapter.BlockListHolder>() {

    class BlockListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var blocked_number_list_item: TextView =
            itemView.findViewById(R.id.blocked_number_list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockListHolder {
        var view =
            LayoutInflater.from(context)
                .inflate(R.layout.show_blocked_number_single_row, parent, false)
        return BlockListHolder(view)
    }

    override fun onBindViewHolder(holder: BlockListHolder, position: Int) {
        val blockedNumber = arrayList[position]
        holder.blocked_number_list_item.text = blockedNumber.phoneNumber
    }

    override fun getItemCount() = arrayList.size
}