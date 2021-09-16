package com.rexoit.cobra

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract.Data.DISPLAY_NAME
import android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI
import android.telephony.TelephonyManager.*
import android.util.Log
import com.rexoit.cobra.service.FloatingWidgetService


private const val TAG = "CallReceiver"

class CallReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val mobileNumber = intent?.getStringExtra(EXTRA_INCOMING_NUMBER)
        val isUnknownNumber: Boolean? = isUnknownCaller(context, mobileNumber)

        isUnknownNumber?.let { unknownNumber ->
            val serviceIntent = Intent(
                context,
                FloatingWidgetService::class.java
            )

            when (intent?.getStringExtra(EXTRA_STATE)) {

                //Incoming Call Detect
                EXTRA_STATE_RINGING -> {
                    Log.d(TAG, "Incoming Call")

                    // check the number is known
                    // if known return
                    // otherwise show cobra button
//                    if (!unknownNumber) return

                    // open floating button when call is ringing
                    // start a service
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context?.startForegroundService(
                            serviceIntent
                        )
                    } else {
                        context?.startService(
                            serviceIntent
                        )
                    }
                }
                else -> {
                    // stop the service
                    context?.stopService(serviceIntent)
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
                contactLookup.moveToNext();
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
}