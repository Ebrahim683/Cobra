package com.rexoit.cobra.service

import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import androidx.annotation.RequiresApi
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.data.model.CallType
import com.rexoit.cobra.utils.CallLogger
import com.rexoit.cobra.utils.SharedPrefUtil
import kotlinx.coroutines.runBlocking

private const val TAG = "CobraCallScreening"

@RequiresApi(Build.VERSION_CODES.N)
class CobraCallScreeningService : CallScreeningService() {
    override fun onScreenCall(call: Call.Details) {
        Log.d(TAG, "onScreenCall: Build")

        val repository = (this.application as CobraApplication).repository
        var isBlockedNumber = false

        Log.d(
            TAG,
            "onScreenCall: Current Shared Pref Number: ${SharedPrefUtil(this).getIncomingNumber()}"
        )

        SharedPrefUtil(this).setIncomingNumber(call.handle.schemeSpecificPart)

        runBlocking {
            val blockedList = repository
                .getBlockedNumberFromNumber(call.handle.schemeSpecificPart)

            // todo: add call log to cobra database
            // repository.addBlockedNumber(callLogInfo)
            
            blockedList?.let {
                isBlockedNumber = true
            }
            Log.d(TAG, "onScreenCall: Blocked Numbers: $blockedList")
        }

        val callResponseBuilder = CallResponse.Builder()
            .setDisallowCall(isBlockedNumber)
            .setRejectCall(isBlockedNumber)
            .setSkipCallLog(isBlockedNumber)
            .setSkipNotification(isBlockedNumber)
            .build()

        respondToCall(call, callResponseBuilder)
    }

}

