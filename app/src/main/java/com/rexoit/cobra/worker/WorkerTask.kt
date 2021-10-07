package com.rexoit.cobra.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkerTask(private val context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    val TAG = "WorkerTask"

    override fun doWork(): Result {
//        sendRejectedNumberCobraDatabase()
        Log.d(TAG, "doWork: WorkManager OK")
        return Result.success()
    }

}