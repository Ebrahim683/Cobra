package com.rexoit.cobra.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private const val TAG = "OkHttpClientInterceptor"

object OkHttpClientInterceptor {
    fun getOkHttpClient(tokenType: String? = "Bearer", token: String): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor { chain: Interceptor.Chain ->

                Log.d(TAG, "getOkHttpClient: Token: $tokenType $token")

                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "$tokenType $token")
                    .addHeader("Accept", "application/json")
                    .build()

                chain.proceed(newRequest)

            }.build()

    fun getCommonOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor { chain: Interceptor.Chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .build()

                chain.proceed(newRequest)

            }.build()
}