package com.rexoit.cobra.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rexoit.cobra.data.model.CallLogInfo

@Dao
interface CobraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBlockedNumber(numbers: CallLogInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBlockedNumbers(numbers: List<CallLogInfo>)

    @Query("SELECT * FROM call_logs")
    suspend fun getBlockedNumbers(): List<CallLogInfo>?

    @Query("SELECT * FROM call_logs WHERE mobileNumber=:mobileNumber")
    suspend fun getBlockedNumberFromNumber(mobileNumber: String): CallLogInfo?
}