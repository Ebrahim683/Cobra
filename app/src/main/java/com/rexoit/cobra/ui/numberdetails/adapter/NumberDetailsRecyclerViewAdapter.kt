package com.rexoit.cobra.ui.numberdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rexoit.cobra.R
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.data.model.CallType
import com.rexoit.cobra.utils.toFormattedElapsedSecondsString
import com.rexoit.cobra.utils.toFormattedTimeString

class NumberDetailsRecyclerViewAdapter(
    private val context: Context,
    private val list: List<CallLogInfo>
) :
    RecyclerView.Adapter<NumberDetailsRecyclerViewAdapter.NumberDetailsViewHolder>() {
    //NumberDetails Call List Item Reference
    class NumberDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callTime: TextView = itemView.findViewById(R.id.sample_call_time)
        val callType: TextView = itemView.findViewById(R.id.sample_call_type)
        val callDuration: TextView = itemView.findViewById(R.id.sample_call_duration)
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
        holder.callTime.text = callLogInfo.time?.toFormattedTimeString().toString()
        holder.callType.text = when (callLogInfo.callType) {
            CallType.INCOMING -> {
                "Incoming call"
            }
            CallType.MISSED -> {
                "Missed call"
            }
            CallType.OUTGOING -> {
                "Outgoing call"
            }
            CallType.BLOCKED -> {
                "Blocked"
            }
            else -> {
                "Incoming call"
            }
        }

        holder.callDuration.text = callLogInfo.time?.toFormattedElapsedSecondsString().toString()
    }

    override fun getItemCount() = list.size

}