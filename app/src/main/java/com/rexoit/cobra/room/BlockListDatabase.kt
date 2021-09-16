package com.rexoit.cobra.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rexoit.cobra.data.model.CallLogInfo

@Database(entities = [CallLogInfo::class], version = 1)
abstract class BlockListDatabase : RoomDatabase() {

    abstract fun getDao(): DatabaseDao

    companion object {
        fun getInstance(context: Context): BlockListDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BlockListDatabase::class.java,
                "block_list_database"
            ).build()
        }
    }

}