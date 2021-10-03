package com.rexoit.cobra.utils

import android.content.Context
import android.content.SharedPreferences

private const val PREFERENCE_NAME = "cobra_utils_SharedPrefUtil"
private const val INCOMING_NUMBER = "com.rexoit.cobra.utils.SharedPrefUtil.INCOMING_NUMBER"
private const val USER_TOKEN = "com.rexoit.cobra.utils.SharedPrefUtil.USER_TOKEN"

class SharedPrefUtil(context: Context) {
    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setUserToken(token: String) {
        // todo: store token
    }

    fun getUserToken(): String? = sharedPreference.getString(USER_TOKEN, null)

    fun setIncomingNumber(number: String?) {
        val editor = sharedPreference.edit()
        editor.putString(INCOMING_NUMBER, number)
        editor.apply()
    }

    fun getIncomingNumber(): String? = sharedPreference.getString(INCOMING_NUMBER, null)

    fun clearSharedPrefUtil() {
        val editor = sharedPreference.edit()
        editor.clear()
        editor.apply()
    }
}