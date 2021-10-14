package com.rexoit.cobra.data.model.user


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User(
    @SerializedName("email")
    @PrimaryKey
    val email: String, // tauhid8k@example.com
    @SerializedName("name")
    val name: String?, // Tauhid
    @SerializedName("phone")
    val phone: String? // 1781380022
) : Parcelable {
    constructor() : this( "", null, null)

    override fun toString(): String {
        return "{" +
                "   name=$name, \n" +
                "   email=$email, \n" +
                "   phone=$phone, \n" +
                "}"
    }
}