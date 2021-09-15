package com.rexoit.cobra.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.rexoit.cobra.data.model.CallLogInfo

@Dao
interface DatabaseDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertData(callLogInfo: CallLogInfo)

    @Delete
    suspend fun deleteData(callLogInfo: CallLogInfo)

    @Query("select * from block_list_table")
    fun getData(): List<CallLogInfo>

}