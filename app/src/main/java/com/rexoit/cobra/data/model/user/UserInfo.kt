package com.rexoit.cobra.data.model.user


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    @SerializedName("user")
    val user: User?
) : Parcelable