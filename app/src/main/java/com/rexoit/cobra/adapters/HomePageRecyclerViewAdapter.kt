package com.rexoit.cobra.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rexoit.cobra.NumberDetailsPage
import com.rexoit.cobra.R
import com.rexoit.cobra.data.model.CallLogInfo

class HomePageRecyclerViewAdapter(
    private val context: Context,
    private val list: List<CallLogInfo>
) :
    RecyclerView.Adapter<HomePageRecyclerViewAdapter.HomePageRecyclerViewHolder>() {

    private lateinit var name: String
    private lateinit var mobileNumber: String
    private lateinit var time: String


    //HomePage Call List Item Reference
    class HomePageRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sample_call_image: ImageView = itemView.findViewById(R.id.sample_call_image)
        val sample_number_text: TextView = itemView.findViewById(R.id.sample_number_text)
        val sample_name_text: TextView = itemView.findViewById(R.id.sample_name_text)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    // Wrapping The Layout Of RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageRecyclerViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.single_row_call_list, parent, false)
        return HomePageRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomePageRecyclerViewHolder, position: Int) {

        //Get The Position Of RecyclerView Item
        val callLogInfo: CallLogInfo = list[position]

        //Get the Data
        name = callLogInfo.name.toString()
        mobileNumber = callLogInfo.mobileNumber.toString()
        time = callLogInfo.time.toString()

        //Binding the item of the recycler view
        holder.sample_call_image.setImageResource(R.drawable.call_icon)
        holder.sample_name_text.text = name
        holder.sample_number_text.text = mobileNumber
        holder.time.text = time

        //Click Event Handler
        onClickItem(holder, name, mobileNumber, time)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //Click Event Handler
    private fun onClickItem(
        holder: HomePageRecyclerViewHolder,
        name: String,
        number: String,
        time: String
    ) {
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, NumberDetailsPage::class.java)
            intent.apply {
                putExtra("name", name)
                putExtra("number", number)
                putExtra("time", time)
            }
            it.context.startActivity(intent)
        }
    }

}