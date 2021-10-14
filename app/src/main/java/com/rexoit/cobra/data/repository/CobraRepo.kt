package com.rexoit.cobra.data.repository

import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.data.model.user.User
import com.rexoit.cobra.data.remote.RetrofitBuilder
import com.rexoit.cobra.data.room.CobraDao

class CobraRepo(private val dao: CobraDao) {
    suspend fun addBlockedNumbers(numbers: List<CallLogInfo>) = dao.addBlockedNumbers(numbers)

    suspend fun addBlockedNumber(number: CallLogInfo) = dao.addBlockedNumber(number)

    suspend fun getBlockedNumbers() = dao.getBlockedNumbers()

    suspend fun getBlockedNumberFromNumber(number: String) = dao.getBlockedNumberFromNumber(number)

    suspend fun sendNumberToCobra(number: String?) {
        val retrofit = RetrofitBuilder.getApiService(null)
        if (number != null) {
            retrofit.addBlockedNumber(number)
        }
    }

    suspend fun setUserInfo(user: User) {
        dao.addUserInfo(user)
    }

    suspend fun getUserInfo(email: String) = dao.getUserInfo(email)
}