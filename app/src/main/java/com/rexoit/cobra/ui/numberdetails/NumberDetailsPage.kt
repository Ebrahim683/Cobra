package com.rexoit.cobra.ui.numberdetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.data.model.CallType
import com.rexoit.cobra.ui.auth.viewmodel.AuthViewModel
import com.rexoit.cobra.ui.main.viewmodel.MainViewModel
import com.rexoit.cobra.ui.numberdetails.adapter.NumberDetailsRecyclerViewAdapter
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_number_details_page.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.*

private const val TAG = "NumberDetailsPage"
const val EXTRA_CALLER_NAME = "com.rexoit.cobra.EXTRA_CALLER_NAME"
const val EXTRA_CALLER_NUMBER = "com.rexoit.cobra.EXTRA_CALLER_NUMBER"
const val EXTRA_CALL_LOGS = "com.rexoit.cobra.EXTRA_CALL_LOGS"
private const val CALL_BACK_REQUEST_CODE = 2021

class NumberDetailsPage : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }
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
        //Block Number Button Work
        block_number_button.setOnClickListener {
            blockNumber()
            sentBlockedNumber()
        }


        //CallBack
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
//                data = Uri.parse("tel:$mobileNumber")
            }
            startActivity(dialIntent)
        }

    }

    private fun blockNumber() {
        if (intent?.getStringExtra(EXTRA_CALLER_NUMBER) != null) {
            runBlocking {
                // add to cobra blocklist database
                val repository = (application as CobraApplication).repository
                repository.getBlockedNumbers().let {
                    Log.d(TAG, "blockNumber: $it")
                }

                Log.d(
                    TAG,
                    "blockNumber: ${intent?.getStringExtra(EXTRA_CALLER_NUMBER)} added to blocked list."
                )
                repository.addBlockedNumber(
                    CallLogInfo(
                        intent?.getStringExtra(EXTRA_CALLER_NUMBER)!!,
                        intent?.getStringExtra(EXTRA_CALLER_NAME),
                        CallType.BLOCKED,
                        Date().time,
                        "0"
                    )
                )
            }
        }
    }

    //sent blocked number to cobra database
    private fun sentBlockedNumber() {
        mobileNumber = mobile_number.text.toString()
        runBlocking {
            viewModel.addBlockedNumber(mobileNumber).collect { resource ->
                Log.d(TAG, "onCreate: Blocked Number Response: " + resource)

                when (resource.status) {
                    Status.SUCCESS -> {
                        Log.d(TAG, "sentBlockedNumber: ${resource.message}")
                        Snackbar.make(
                            number_details_layout_id,
                            "Number Blocked",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "sentBlockedNumber: ${resource.message}")
                        Snackbar.make(
                            number_details_layout_id,
                            "Number Block Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    Status.LOADING -> {
                        Log.d(TAG, "sentBlockedNumber: Loading...")
                    }
                    Status.UNAUTHORIZED -> {

                    }
                }
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