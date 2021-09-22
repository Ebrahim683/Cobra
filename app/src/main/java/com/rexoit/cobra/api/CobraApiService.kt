package com.rexoit.cobra.api

import com.rexoit.cobra.api.response.BlockedNumberResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CobraApiService {

    companion object {
        //change url
        const val BASE_URL = "cobraUrl"
    }

    //sent blocked number to database
    @FormUrlEncoded
    @POST("DEMO")
    fun setBlockedNumber(
        @Field("blockedNumber") blockedNumber: String
    ): Call<BlockedNumberResponse>

}