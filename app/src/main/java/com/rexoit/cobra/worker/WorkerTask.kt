package com.rexoit.cobra.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rexoit.cobra.api.RetrofitClient
import com.rexoit.cobra.api.response.RejectNumberSentDatabaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkerTask(private val context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    val TAG = "WorkerTask"

    override fun doWork(): Result {
//        sendRejectedNumberCobraDatabase()
        Log.d(TAG, "doWork: WorkManager OK")
        return Result.success()
    }

    private fun sendRejectedNumberCobraDatabase() {

        val getRejectedNumber = inputData.getString("rejected_number")
        val rejectNumberCall = RetrofitClient().apiService.sendRejectedNumber(getRejectedNumber!!)

        rejectNumberCall.enqueue(object : Callback<RejectNumberSentDatabaseResponse> {
            override fun onResponse(
                call: Call<RejectNumberSentDatabaseResponse>,
                response: Response<RejectNumberSentDatabaseResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Success")
                }
            }

            override fun onFailure(call: Call<RejectNumberSentDatabaseResponse>, t: Throwable) {
                Log.d(TAG, "onResponse: Failed")
            }
        })

    }

}