package com.rexoit.cobra.service

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import androidx.annotation.RequiresApi

private const val TAG = "CobraCallScreening"

@RequiresApi(Build.VERSION_CODES.N)
class CobraCallScreeningService : CallScreeningService() {
    override fun onScreenCall(call: Call.Details) {
        Log.d(TAG, "onScreenCall: Build")
        Log.d(TAG, "onScreenCall: Caller Name: ${call.callerDisplayName}")

       val callResponseBuilder = CallResponse.Builder().build()
//            .setDisallowCall(true)
//            .setRejectCall(true)
//            .build()

        respondToCall(call, callResponseBuilder)
    }
}