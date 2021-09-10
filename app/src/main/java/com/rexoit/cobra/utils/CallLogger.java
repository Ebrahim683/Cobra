package com.rexoit.cobra.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.rexoit.cobra.data.model.CallLogInfo;
import com.rexoit.cobra.data.model.CallType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogger {
    public static List<CallLogInfo> getCallDetails(Context context) {

        List<CallLogInfo> callLogInfoList = new ArrayList<>();

        Cursor managedCursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
        );

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String mobileNumber = managedCursor.getString(number);
            String username = managedCursor.getString(name);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);

            CallType mCallType = null;

            int callDirectionCode = Integer.parseInt(callType);

            switch (callDirectionCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    mCallType = CallType.OUTGOING;
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    mCallType = CallType.INCOMING;
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    mCallType = CallType.MISSED;
                    break;
            }

            callLogInfoList.add(
                    new CallLogInfo(
                            username != null ? username.isEmpty() ? "Unknown Caller" : username : "Unknown Caller",
                            mobileNumber,
                            mCallType,
                            callDayTime.getTime(),
                            callDuration
                    )
            );
        }
        managedCursor.close();
        return callLogInfoList;
    }
}
