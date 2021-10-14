package com.rexoit.cobra.data.model.blocklist


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlockListNumbers(
    @SerializedName("numbers")
    val numbers: List<Number>?
) : Parcelable