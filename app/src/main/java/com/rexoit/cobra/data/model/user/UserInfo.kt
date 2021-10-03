package com.rexoit.cobra.data.model.user


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    @SerializedName("user")
    val user: User?
) : Parcelable