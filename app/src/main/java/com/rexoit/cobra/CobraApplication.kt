package com.rexoit.cobra

import android.app.Application
import android.util.Log
import com.rexoit.cobra.data.repository.CobraRepo
import com.rexoit.cobra.data.room.CobraDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

private const val TAG = "CobraApplication"

class CobraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy {
        Log.d(TAG, "onCreate: Initializing Database .......")
        CobraDatabase.getDatabase(this, applicationScope)
    }

    val repository by lazy {
        Log.d(TAG, "onCreate: Initializing Repository.......")
        CobraRepo(database.cobraDao())
    }
}