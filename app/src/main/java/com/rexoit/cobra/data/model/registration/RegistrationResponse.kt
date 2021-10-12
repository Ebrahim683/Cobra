package com.rexoit.cobra.data.model.registration


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegistrationResponse(
    @SerializedName("message")
    val message: String? // Registration Successful
) : Parcelable