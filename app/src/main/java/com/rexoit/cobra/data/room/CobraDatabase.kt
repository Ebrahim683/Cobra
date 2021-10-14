package com.rexoit.cobra.data.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.data.model.Converter
import com.rexoit.cobra.data.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val DATABASE_VERSION = 5
private const val DATABASE_NAME = "CobraRoomDatabase"

private const val TAG = "CobraDatabase"

@Database(
    entities = [
        CallLogInfo::class,
        User::class
    ], version = DATABASE_VERSION, exportSchema = false
)
@TypeConverters(Converter::class)
abstract class CobraDatabase : RoomDatabase() {
    abstract fun cobraDao(): CobraDao

    private class CobraDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            INSTANCE?.let { database ->
                scope.launch {
                    Log.d(TAG, "onCreate: Database Updated!")
                    populateDatabase(database.cobraDao())
                }
            }
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    Log.d(TAG, "onCreate: Database Created!")
                    populateDatabase(database.cobraDao())
                }
            }
        }

        private fun populateDatabase(database: CobraDao) {
            Log.d(TAG, "populateDatabase: Database Initialised!")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CobraDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CobraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CobraDatabase::class.java,
                    DATABASE_NAME
                ).addCallback(CobraDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}