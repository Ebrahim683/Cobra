package com.rexoit.cobra

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.adapters.NumberDetailsRecyclerViewAdapter
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.room.BlockListDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_number_details_page.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "NumberDetailsPage"
const val EXTRA_CALLER_NAME = "com.rexoit.cobra.EXTRA_CALLER_NAME"
const val EXTRA_CALLER_NUMBER = "com.rexoit.cobra.EXTRA_CALLER_NUMBER"
const val EXTRA_CALL_LOGS = "com.rexoit.cobra.EXTRA_CALL_LOGS"

class NumberDetailsPage : AppCompatActivity() {

    private lateinit var database: BlockListDatabase
    private lateinit var mobileNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_details_page)

        Log.d(TAG, "onCreate: Created!")


        val toolbar = number_tool_bar_id
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Receive and set name and number from HomePage
        unknown_text.text = intent?.getStringExtra(EXTRA_CALLER_NAME)
        mobile_number.text = intent?.getStringExtra(EXTRA_CALLER_NUMBER)

        //get call logs for selected mobile number from intent that pass from parent intent
        val callLogForToday = intent.getParcelableArrayListExtra<CallLogInfo>(EXTRA_CALL_LOGS)

        callLogForToday?.let { callLogs ->
            val numberAdapter = NumberDetailsRecyclerViewAdapter(this, callLogs)
            rec_id_call.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@NumberDetailsPage)
                adapter = numberAdapter
            }
        }

        //Block Number
        database = BlockListDatabase.getInstance(this)
        block_number_button.setOnClickListener {
            blockNumber()
        }
        //Fetch blocked number (LogCat)
        GlobalScope.launch {
            database.getDao().getData().forEach {
                Log.d(TAG, "Name: ${it.name}, Number: ${it.mobileNumber}")
            }
        }

        //CallBack
        val CALL_BACK_REQUEST_CODE = 2021
        call_button_id.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_BACK_REQUEST_CODE
                )
            } else {
                mobileNumber = mobile_number.text.toString()
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.apply {
                    data = Uri.parse("tel:$mobileNumber")
                }
                startActivity(callIntent)
            }
        }

        //Send Message
        sms_button_id.setOnClickListener {
            mobileNumber = mobile_number.text.toString()
            val smsIntent = Intent(Intent.ACTION_SENDTO)
            smsIntent.apply {
                data = Uri.parse("sms:$mobileNumber")
            }
            startActivity(smsIntent)
        }

        //New Dial
        dial_button_id.setOnClickListener {
            mobileNumber = mobile_number.text.toString()
            val dialIntent = Intent()
            dialIntent.apply {
                action = Intent.ACTION_DIAL
                data = Uri.parse("tel:$mobileNumber")
            }
            startActivity(dialIntent)
        }

    }

    private fun blockNumber() {
        if (mobile_number != null) {
            val name = unknown_text.text.toString()
            val number = mobile_number.text.toString()
            GlobalScope.launch {
                database.getDao().insertData(CallLogInfo(name, number, null, null, null))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.number_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.edit -> {
                Snackbar.make(number_details_layout_id, "Edit", Snackbar.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}