package com.rexoit.cobra.data.model.user


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("message")
    val message: String?, // Login Successful
    @SerializedName("token")
    val token: String? // 4|aS6y1B4gBUR3bEJ7SyIpKYuDoDIo6lNCL4eGo4t1
) : Parcelable