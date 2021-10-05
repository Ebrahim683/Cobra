package com.rexoit.cobra.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "http://cobra.everestbajar.com/"

    private fun getRetrofit(client: OkHttpClient?): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val instance = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) //important
            .addConverterFactory(GsonConverterFactory.create(gson))

        client?.let { instance.client(client) }

        return instance.build()
    }

    fun getApiService(client: OkHttpClient?): CobraApiService =
        getRetrofit(client).create(CobraApiService::class.java)
}