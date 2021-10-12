package com.rexoit.cobra.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

private const val PREFERENCE_NAME = "cobra_utils_SharedPrefUtil"
private const val INCOMING_NUMBER = "com.rexoit.cobra.utils.SharedPrefUtil.INCOMING_NUMBER"
private const val USER_TOKEN = "com.rexoit.cobra.utils.SharedPrefUtil.USER_TOKEN"

private const val TAG = "SharedPrefUtil"

class SharedPrefUtil(context: Context) {
    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setUserToken(token: String) {
        val editor = sharedPreference.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getUserToken(): String? = sharedPreference.getString(USER_TOKEN, null)

    fun setIncomingNumber(number: String?) {
        Log.d(TAG, "setIncomingNumber: Call log. Number: $number")
        val editor = sharedPreference.edit()
        editor.putString(INCOMING_NUMBER, number)
        editor.apply()
    }

    fun getIncomingNumber(): String? = sharedPreference.getString(INCOMING_NUMBER, null)

    fun clearIncomingNumberPref() {
        val editor = sharedPreference.edit()
        editor.remove(INCOMING_NUMBER)
        editor.apply()
    }

    fun clearAccessTokenPref() {
        val editor = sharedPreference.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }
}