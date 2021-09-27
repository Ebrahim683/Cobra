package com.rexoit.cobra.data.model.user


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("blockedNumbers")
    val blockedNumbers: List<BlockedNumber>?,
    @SerializedName("email")
    val email: String?, // tauhid8k@example.com
    @SerializedName("name")
    val name: String?, // Tauhid
    @SerializedName("phone")
    val phone: String? // 1781380022
) : Parcelable