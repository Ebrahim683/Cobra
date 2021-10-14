package com.rexoit.cobra.data.model.blocklist


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddToBlockListResponse(
    @SerializedName("message")
    val message: String? // Number synced with cobra successfully
) : Parcelable