package com.rexoit.cobra

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

private const val TAG = "CallReceiver"

class CallReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val mobileNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        when(intent?.getStringExtra(TelephonyManager.EXTRA_STATE)) {

            //Incoming Call Detect
            TelephonyManager.EXTRA_STATE_RINGING -> {
                Log.d(TAG, "Incoming Call")
                Toast.makeText(context, "Incoming Call: $mobileNumber", Toast.LENGTH_SHORT).show()
            }

            //Outgoing Call Detect
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                Log.d(TAG, "Outgoing Call")
                Toast.makeText(context, "Outgoing Call $mobileNumber", Toast.LENGTH_SHORT).show()
            }

            //Call End Detect
            TelephonyManager.EXTRA_STATE_IDLE -> {
                Log.d(TAG, "Call Ended")
                Toast.makeText(context, "Call Ended $mobileNumber", Toast.LENGTH_SHORT).show()
            }
        }
    }
}