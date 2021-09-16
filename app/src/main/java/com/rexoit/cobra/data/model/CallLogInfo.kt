package com.rexoit.cobra.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CallLogInfo(
    val name: String?,
    val mobileNumber: String?,
    val callType: CallType?,
    val time: Long?,
    val duration: String?
) : Parcelable {
    override fun toString(): String {
        return "{" +
                "   name=$name, \n" +
                "   mobileNumber=$mobileNumber, \n" +
                "   callType=$callType, \n" +
                "   time=$time, \n" +
                "   duration=$duration\n" +
                "}"
    }

}