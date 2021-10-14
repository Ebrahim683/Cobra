package com.rexoit.cobra.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rexoit.cobra.data.remote.RetrofitBuilder
import com.rexoit.cobra.utils.OkHttpClientInterceptor
import kotlinx.coroutines.runBlocking

private const val TAG = "WorkerTask"

class WorkerTask(context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        val token = workerParameters.inputData.getString("access_token")
        val number = workerParameters.inputData.getString("rejected_number")

        if (token != null && number != null) {
            val retrofit = RetrofitBuilder.getApiService(
                OkHttpClientInterceptor.getOkHttpClient(token = token)
            )

            runBlocking {
                try {
                    retrofit.addBlockedNumber(number)
                } catch (e: Exception) {
                    Log.e(TAG, "doWork: ", e)
                }
            }
        }


        return Result.success()
    }

}