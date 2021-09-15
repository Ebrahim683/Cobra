package com.rexoit.cobra.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "block_list_table")
data class CallLogInfo(
    val name: String?,
    val mobileNumber: String?,
    val callType: CallType?,
    val time: Long?,
    val duration: String?
) : Parcelable {
    @PrimaryKey(autoGenerate = true) var id = 0
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