package com.rexoit.cobra.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract.Data.DISPLAY_NAME
import android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import android.util.Log
import androidx.core.app.ActivityCompat
import com.android.internal.telephony.ITelephony
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.service.FloatingWidgetService
import com.rexoit.cobra.utils.CallLogger
import com.rexoit.cobra.utils.SharedPrefUtil
import kotlinx.coroutines.runBlocking


private const val TAG = "CallReceiver"

class CallReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val mobileNumber = intent?.getStringExtra(EXTRA_INCOMING_NUMBER)
        val isUnknownNumber: Boolean? = isUnknownCaller(context, mobileNumber)

        if (context == null) return
        val sharedPrefUtil = SharedPrefUtil(context)
        sharedPrefUtil.setIncomingNumber(mobileNumber)

        isUnknownNumber?.let { unknownNumber ->
            val serviceIntent = Intent(
                context,
                FloatingWidgetService::class.java
            )

            when (intent?.getStringExtra(EXTRA_STATE)) {
                //Incoming Call Detect
                EXTRA_STATE_RINGING -> {
                    Log.d(TAG, "Incoming Call")

                    mobileNumber?.let {
                        val repository =
                            (context.applicationContext as CobraApplication).repository

                        var isBlockedNumber = false

                        runBlocking {
                            val blockedList = repository
                                .getBlockedNumberFromNumber(mobileNumber)

                            blockedList?.let {
                                isBlockedNumber = true
                            }
                            Log.d(TAG, "onReceive: Blocked Numbers: $blockedList")
                        }

                        if (isBlockedNumber) {
                            handleCall(context)
                            return
                        }

                        if (unknownNumber) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(
                                    serviceIntent
                                )
                            } else {
                                context.startService(
                                    serviceIntent
                                )
                            }
                        }
                    }
                }
                else -> {
                    try {
                        val callLogs = CallLogger.getCallDetails(context)
                        // todo: store call logs to database
                        val repository = (context as CobraApplication).repository
                        runBlocking {
                            repository.addBlockedNumbers(callLogs)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onReceive: Failed to read call logs.", e)
                    }

                    // stop the service
                    context.stopService(serviceIntent)
                    sharedPrefUtil.clearSharedPrefUtil()
                }
            }
        }
    }

    @SuppressLint("Range", "Recycle")
    private fun isUnknownCaller(context: Context?, mobileNumber: String?): Boolean? {
        if (mobileNumber == null) return null

        val uri: Uri = Uri.withAppendedPath(
            CONTENT_FILTER_URI,
            Uri.encode(mobileNumber)
        )

        val contactLookup = context?.contentResolver?.query(
            uri, null, null, null, null
        )

        var name: String? = null

        try {
            if (contactLookup != null && contactLookup.count > 0) {
                contactLookup.moveToNext()
                name = contactLookup.getString(
                    contactLookup.getColumnIndex(DISPLAY_NAME)
                )
                // this.id =
                // contactLookup.getString(contactLookup.getColumnIndex(CONTACT_ID));
                // String contactId =
                // contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } catch (e: Exception) {
            Log.e(TAG, "isUnknownCaller: ", e)
        }

        return name == null
    }

    private fun handleCall(context: Context) {
        val telephonyService = getTeleService(context)
        if (telephonyService != null) {
            try {
                telephonyService.silenceRinger()
                telephonyService.endCall()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("TAG", "CATCH CALL")
            }
        } else {
            // handle call disconnect api level 28+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val mgr = context.getSystemService(Service.TELECOM_SERVICE) as TelecomManager
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ANSWER_PHONE_CALLS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                if (!mgr.isInCall) return
                mgr.endCall()
            }
        }
    }

    private fun getTeleService(context: Context): ITelephony? {
        val tm = context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        try {
            val telephonyMethod =
                Class.forName(tm.javaClass.name).getDeclaredMethod("getITelephony")
            telephonyMethod.isAccessible = true
            return telephonyMethod.invoke(tm) as ITelephony
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}