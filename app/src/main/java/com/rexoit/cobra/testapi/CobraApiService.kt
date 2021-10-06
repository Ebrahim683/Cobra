package com.rexoit.cobra.testapi

import com.rexoit.cobra.testapi.response.BlockedNumberResponse
import com.rexoit.cobra.testapi.response.RejectNumberSentDatabaseResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CobraApiService {

    companion object {
        const val BASE_URL = "cobra.everestbajar.com/api/"
    }

    //sent blocked number to database
    @FormUrlEncoded
    @POST("END_POINT")
    fun sendBlockedNumber(
        @Field("blockedNumber") blockedNumber: String
    ): Call<BlockedNumberResponse>


    //sent rejected number to cobra database
    @FormUrlEncoded
    @POST("END_POINT")
    fun sendRejectedNumber(
        @Field("rejectNumber") rejectNumber:String
    ):Call<RejectNumberSentDatabaseResponse>

}