package com.rexoit.cobra.data.model

import androidx.room.TypeConverter

enum class CallType {
    INCOMING,
    MISSED,
    OUTGOING,
    BLOCKED
}

class Converter {
    @TypeConverter
    fun fromCallType(type: CallType): String {
        return type.name
    }

    @TypeConverter
    fun toPriority(type: String): CallType {
        return CallType.valueOf(type)
    }

}