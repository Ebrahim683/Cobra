package com.rexoit.cobra.ui.main.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rexoit.cobra.*
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.ui.numberdetails.EXTRA_CALLER_NAME
import com.rexoit.cobra.ui.numberdetails.EXTRA_CALLER_NUMBER
import com.rexoit.cobra.ui.numberdetails.EXTRA_CALL_LOGS
import com.rexoit.cobra.ui.numberdetails.NumberDetailsPage
import com.rexoit.cobra.utils.getCurrentDayDiff
import com.rexoit.cobra.utils.toFormattedDateString
import com.rexoit.cobra.utils.toFormattedElapsedTimeString
import java.util.*

private const val TAG = "HomePageRecyclerViewAda"

class HomePageRecyclerViewAdapter() :
    ListAdapter<CallLogInfo, HomePageRecyclerViewAdapter.HomePageRecyclerViewHolder>(
        DIFF_UTIL_CALLBACK
    ) {

    //HomePage Call List Item Reference
    class HomePageRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImage: ImageView = itemView.findViewById(R.id.sample_call_image)
        val mCallerMobileNumber: TextView = itemView.findViewById(R.id.sample_number_text)
        val mCallerName: TextView = itemView.findViewById(R.id.sample_name_text)
        val mCallTime: TextView = itemView.findViewById(R.id.time)
    }

    // Wrapping The Layout Of RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageRecyclerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_row_call_list, parent, false)
        return HomePageRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomePageRecyclerViewHolder, position: Int) {

        //Get The Position Of RecyclerView Item
        val callLogInfo: CallLogInfo = getItem(position)

        //Binding the item of the recycler view
        holder.mImage.setImageResource(R.drawable.call_icon)
        holder.mCallerName.text = callLogInfo.callerName
        holder.mCallerMobileNumber.text = callLogInfo.mobileNumber
        holder.mCallTime.text = callLogInfo.time?.toFormattedElapsedTimeString()

        Log.d(TAG, "onBindViewHolder: ${callLogInfo.time?.getCurrentDayDiff()}")

        //Click Event Handler
        onClickItem(
            holder,
            callLogInfo.callerName,
            callLogInfo.mobileNumber
        )
    }

    //Click Event Handler
    private fun onClickItem(
        holder: HomePageRecyclerViewHolder,
        name: String?,
        number: String?,
    ) {
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, NumberDetailsPage::class.java)

            val sortedCallListForNumber = currentList.filter { current ->
                current.mobileNumber == number &&
                        current.time?.toFormattedDateString() == Date().time.toFormattedDateString()
            }

            val sortedCallLogArrayList = arrayListOf<CallLogInfo>()
            sortedCallLogArrayList.addAll(sortedCallListForNumber)

            intent.apply {
                putExtra(EXTRA_CALLER_NAME, name)
                putExtra(EXTRA_CALLER_NUMBER, number)
                putParcelableArrayListExtra(EXTRA_CALL_LOGS, sortedCallLogArrayList)
            }
            it.context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<CallLogInfo>() {
            override fun areItemsTheSame(oldItem: CallLogInfo, newItem: CallLogInfo): Boolean {
                return oldItem.mobileNumber == newItem.mobileNumber && oldItem.time == newItem.time && oldItem.callerName == newItem.callerName && oldItem.callType == newItem.callType && oldItem.duration == newItem.duration
            }

            override fun areContentsTheSame(oldItem: CallLogInfo, newItem: CallLogInfo): Boolean {
                return oldItem.mobileNumber == newItem.mobileNumber && oldItem.time == newItem.time && oldItem.callerName == newItem.callerName && oldItem.callType == newItem.callType && oldItem.duration == newItem.duration
            }

        }
    }
}