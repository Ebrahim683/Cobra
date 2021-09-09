package com.rexoit.cobra.data.model

data class CallLogInfo(
    val name:String?,
    val mobileNumber:String?,
    val callType:CallType?,
    val time:String?,
    val duration:String?
){
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