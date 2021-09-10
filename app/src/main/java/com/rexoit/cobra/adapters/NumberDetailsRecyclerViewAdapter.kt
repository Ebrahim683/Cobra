package com.rexoit.cobra.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rexoit.cobra.R
import com.rexoit.cobra.data.model.CallLogInfo

class NumberDetailsRecyclerViewAdapter(
    private val context: Context,
    private val list: List<CallLogInfo>
) :
    RecyclerView.Adapter<NumberDetailsRecyclerViewAdapter.NumberDetailsViewHolder>() {

    private lateinit var callTime: String
    private lateinit var callType: String
    private lateinit var callDuration: String

    //NumberDetails Call List Item Reference
    class NumberDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sample_call_time: TextView = itemView.findViewById(R.id.sample_call_time)
        val sample_call_type: TextView = itemView.findViewById(R.id.sample_call_type)
        val sample_call_duration: TextView = itemView.findViewById(R.id.sample_call_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberDetailsViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.single_row_call_history_layout, parent, false)
        return NumberDetailsViewHolder(view)
    }

    // Wrapping The Layout Of RecyclerView
    override fun onBindViewHolder(holder: NumberDetailsViewHolder, position: Int) {

        //Get The Position Of RecyclerView Item
        val callLogInfo: CallLogInfo = list[position]

        //Get the Data
        callTime = callLogInfo.time.toString()
        callType = callLogInfo.callType.toString()
        callDuration = callLogInfo.duration.toString()

        //Binding the item of the recycler view
        holder.sample_call_time.text = callTime
        holder.sample_call_type.text = callType
        holder.sample_call_duration.text = callDuration

    }

    override fun getItemCount(): Int {
        return list.size
    }

}