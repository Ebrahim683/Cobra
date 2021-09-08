package com.rexoit.cobra

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        when {

            //Incoming Call Detect
            intent?.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING -> {
                Log.d("callStatus", "Incoming Call")
            }

            //Outgoing Call Detect
            intent?.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                Log.d("callStatus", "Outgoing Call")
            }

            //Call End Detect
            intent?.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE -> {
                Log.d("callStatus", "Call Ended")
            }
        }
    }
}