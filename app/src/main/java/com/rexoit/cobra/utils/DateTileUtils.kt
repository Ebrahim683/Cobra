package com.rexoit.cobra.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DateTileUtils"

@SuppressLint("SimpleDateFormat")
fun Long.toFormattedDateTimeString(): String {
    val simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy hh:mm:ss aa")
    return simpleDateFormat.format(Date(this))
}

@SuppressLint("SimpleDateFormat")
fun Long.toFormattedDateString(): String {
    val simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy")
    return simpleDateFormat.format(Date(this))
}

@SuppressLint("SimpleDateFormat")
fun Long.toFormattedTimeString(): String {
    val simpleDateFormat = SimpleDateFormat("hh:mm aa")
    return simpleDateFormat.format(Date(this))
}

fun Long.getCurrentDayDiff(): Long {
    var different: Long = Date().time - this

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24

    val elapsedDays = different / daysInMilli;
    different %= daysInMilli;

    return elapsedDays
}

fun Long.toFormattedElapsedTimeString(): String {
    var different: Long = Date().time - this

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24

    val elapsedDays = different / daysInMilli;
    different %= daysInMilli;

    val elapsedHours = different / hoursInMilli
    different %= hoursInMilli

    val elapsedMinutes = different / minutesInMilli
    different %= minutesInMilli

    val elapsedSeconds = different / secondsInMilli

    val stringBuilder = StringBuilder()


    if (elapsedDays > 0) {
        return this.toFormattedDateString()
//        stringBuilder.append("$elapsedDays Days")
    } else {
        if (elapsedHours > 0) {
            when {
                elapsedHours > 24 -> {
                    return this.toFormattedDateString()
                }
                elapsedHours > 1 -> {
                    return this.toFormattedTimeString()
                }
                else -> {
                    stringBuilder.append("$elapsedHours Hour, $elapsedMinutes Min")
                }
            }
        } else {
            if (elapsedMinutes > 0 && elapsedSeconds > 0) {
                if (elapsedHours > 1) {
                    return this.toFormattedTimeString()
                } else {
                    stringBuilder.append("$elapsedMinutes Min, $elapsedSeconds sec")
                }
            } else if (elapsedMinutes > 0) {
                stringBuilder.append("$elapsedMinutes Min")
            } else if (elapsedSeconds > 0) {
                stringBuilder.append("$elapsedSeconds Sec")
            }
        }
    }

    return stringBuilder.toString()
}


fun Long.toFormattedElapsedSecondsString(): String {
    var different: Long = Date().time - this

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24

    val elapsedDays = different / daysInMilli;
    different %= daysInMilli;

    val elapsedHours = different / hoursInMilli
    different %= hoursInMilli

    val elapsedMinutes = different / minutesInMilli
    different %= minutesInMilli

    val elapsedSeconds = different / secondsInMilli

    val stringBuilder = StringBuilder()

    if (elapsedHours > 0) {
        stringBuilder.append("$elapsedHours Hour, $elapsedMinutes Min")
    } else {
        if (elapsedMinutes > 0 && elapsedSeconds > 0) {
            stringBuilder.append("$elapsedMinutes Min, $elapsedSeconds sec")
        } else if (elapsedMinutes > 0) {
            stringBuilder.append("$elapsedMinutes Min, $elapsedSeconds Sec")
        } else if (elapsedSeconds > 0) {
            stringBuilder.append("0 Min, $elapsedSeconds Sec")
        }
    }

    return stringBuilder.toString()
}
