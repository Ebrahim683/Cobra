package com.rexoit.cobra.data.remote

import com.rexoit.cobra.data.model.blocklist.AddToBlockListResponse
import com.rexoit.cobra.data.model.blocklist.BlockListNumbers
import com.rexoit.cobra.data.model.registration.RegistrationResponse
import com.rexoit.cobra.data.model.user.LoginResponse
import com.rexoit.cobra.data.model.user.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface CobraApiService {

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("api/register")
    suspend fun registration(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
    ): RegistrationResponse

    @FormUrlEncoded
    @POST("api/email-verify")
    suspend fun verifyEmailAddress(
        @Field("email") email: String,
        @Field("verification_code") verificationCode: String,
    ): Any

    @FormUrlEncoded
    @POST("api/resend-email-verify")
    suspend fun resendVerificationEmail(
        @Field("email") email: String,
    ): Any

    @FormUrlEncoded
    @POST("api/blocked-numbers")
    suspend fun addBlockedNumber(
        @Field("phone_number") phone: String,
    ): AddToBlockListResponse

    @GET("api/blocked-numbers")
    suspend fun getBlockedNumbers(): BlockListNumbers

    @GET("api/user")
    suspend fun getUserInfo(): UserInfo

    @POST("/logout")
    suspend fun logout(): Any
}