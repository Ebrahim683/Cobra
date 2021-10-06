package com.rexoit.cobra.testapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    val apiService: CobraApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(CobraApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(CobraApiService::class.java)
    }

}