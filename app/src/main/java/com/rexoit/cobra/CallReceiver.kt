package com.rexoit.cobra

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import android.provider.ContactsContract.Data.DISPLAY_NAME
import android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI
import android.telephony.TelephonyManager.*
import java.lang.Exception


private const val TAG = "CallReceiver"

class CallReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val mobileNumber = intent?.getStringExtra(EXTRA_INCOMING_NUMBER)
        val isUnknownNumber: Boolean? = isUnknownCaller(context, mobileNumber)

        isUnknownNumber?.let {
            when (intent?.getStringExtra(EXTRA_STATE)) {

                //Incoming Call Detect
                EXTRA_STATE_RINGING -> {
                    Log.d(TAG, "Incoming Call")
                    Toast.makeText(
                        context,
                        "Incoming Call: $mobileNumber. Is Unknown number: $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Outgoing Call Detect
                EXTRA_STATE_OFFHOOK -> {
                    Log.d(TAG, "Outgoing Call")
                    Toast.makeText(
                        context,
                        "Outgoing Call $mobileNumber. Is Unknown number: $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //Call End Detect
                EXTRA_STATE_IDLE -> {
                    Log.d(TAG, "Call Ended")
                    Toast.makeText(
                        context,
                        "Call Ended $mobileNumber. Is Unknown number: $it",
                        Toast.LENGTH_SHORT
                    ).show()
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