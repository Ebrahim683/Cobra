package com.rexoit.cobra.data.model.blocklist


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Number(
    @SerializedName("created_at")
    val createdAt: String?, // 2021-09-26T11:32:10.000000Z
    @SerializedName("id")
    val id: Int?, // 1
    @SerializedName("phone_number")
    val phoneNumber: String?, // 01781380022
    @SerializedName("updated_at")
    val updatedAt: String?, // 2021-09-26T11:32:10.000000Z
    @SerializedName("user_id")
    val userId: String? // 1
) : Parcelable