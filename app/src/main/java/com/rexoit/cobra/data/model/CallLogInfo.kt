package com.rexoit.cobra.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "call_logs")
data class CallLogInfo(
    @PrimaryKey
    var mobileNumber: String,
    val callerName: String?,
    var callType: CallType?,
    val time: Long?,
    val duration: String?
) : Parcelable {
    constructor() : this("", null, null, null, null)

    override fun toString(): String {
        return "{" +
                "   name=$callerName, \n" +
                "   mobileNumber=$mobileNumber, \n" +
                "   callType=$callType, \n" +
                "   time=$time, \n" +
                "   duration=$duration\n" +
                "}"
    }

}