package com.rexoit.cobra

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.adapters.NumberDetailsRecyclerViewAdapter
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.data.model.CallType
import kotlinx.android.synthetic.main.activity_number_details_page.*

class NumberDetailsPage : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var name: String
    private lateinit var number: String
    private lateinit var numberAdapter: NumberDetailsRecyclerViewAdapter
    private lateinit var arrayList: ArrayList<CallLogInfo>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_details_page)

        toolbar = findViewById(R.id.number_tool_bar_id)
        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)


        btn_back.setOnClickListener {
            finish()
        }

        //Receive and set name and number from HomePage
        name = intent?.getStringExtra("name").toString()
        number = intent?.getStringExtra("number").toString()
        unknown_text.text = name
        mobile_number.text = number

        //RecyclerView Work
        arrayList = ArrayList()

        val incoming: CallType = CallType.INCOMING
        val outgoing: CallType = CallType.OUTGOING
        val missed: CallType = CallType.MISSED
        arrayList.add(CallLogInfo(null, null, incoming, "1 min ago", "45 second"))
        arrayList.add(CallLogInfo(null, null, outgoing, "31 min ago", "1 hours"))
        arrayList.add(CallLogInfo(null, null, missed, "1 hour ago", "30 second"))

        numberAdapter = NumberDetailsRecyclerViewAdapter(this, arrayList)
        rec_id_call.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@NumberDetailsPage)
            adapter = numberAdapter
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.number_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            android.R.id.home ->{
//                finish()
//            }
            R.id.edit -> {
                Snackbar.make(number_details_layout_id, "Edit", Snackbar.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}