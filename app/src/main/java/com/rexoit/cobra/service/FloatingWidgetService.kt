package com.rexoit.cobra.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.android.internal.telephony.ITelephony
import com.rexoit.cobra.R
import com.rexoit.cobra.utils.SharedPrefUtil
import com.rexoit.cobra.worker.WorkerTask
import kotlin.math.ceil


/**
 * Created by sonu on 28/03/17.
 * url: https://www.androhub.com/android-floating-widget-like-facebook-messenger-chat-head/
 */
private const val CHANNEL_ID = "cobra_channel_001"

class FloatingWidgetService : Service(), View.OnClickListener {
    private var mWindowManager: WindowManager? = null
    private var mFloatingWidgetView: View? = null
    private val szWindow = Point()
    private var removeFloatingWidgetView: View? = null

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind: Created")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: FloatingWidgetService")
        val channel: NotificationChannel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                CHANNEL_ID,
                "Notification from cobra",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Cobra")
                .setContentText("Cobra is running")
                .build()
            startForeground(NOTIFICATION_ID, notification)
        }

        //init WindowManager
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        Log.d(TAG, "onCreate: Notification Created!")

        getWindowManagerDefaultDisplay()

        Log.d(TAG, "onCreate: Creating floating button")
        //Init LayoutInflater
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        addFloatingWidgetView(inflater)
        implementTouchListenerToFloatingWidgetView()
        Log.d(TAG, "onCreate: Floating button showed!")
    }

    /*  Add Floating Widget View to Window Manager  */
    @SuppressLint("InflateParams")
    private fun addFloatingWidgetView(inflater: LayoutInflater) {
        //Inflate the floating view layout we created
        mFloatingWidgetView = inflater.inflate(R.layout.floating_widget_layout, null)
        val layoutParams: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        //Add the view to the window.
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParams,
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        //Specify the view position
        params.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL

        //Initially view will be added to top-left corner, you change x-y coordinates according to your need
        params.x = 0
        params.y = 100

        //Add the view to the window
        mWindowManager!!.addView(mFloatingWidgetView, params)
    }

    private fun getWindowManagerDefaultDisplay() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) { mWindowManager!!.defaultDisplay.getSize(
//                szWindow
//            )
//        } else {
//            val w = mWindowManager!!.defaultDisplay.width
//            val h = mWindowManager!!.defaultDisplay.height
//            szWindow[w] = h
//        }
    }

//    fun getScreenWidth(activity: Activity): Int {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val windowMetrics = activity.windowManager.currentWindowMetrics
//            val insets: Insets = windowMetrics.windowInsets
//                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
//            windowMetrics.bounds.width() - insets.left - insets.right
//        } else {
//            val displayMetrics = DisplayMetrics()
//            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
//            displayMetrics.widthPixels
//        }
//    }

    /*  Implement Touch Listener to Floating Widget Root View  */
    @SuppressLint("ClickableViewAccessibility")
    private fun implementTouchListenerToFloatingWidgetView() {
        //Drag and move floating view using user's touch action.
        mFloatingWidgetView!!.findViewById<View>(R.id.root_container)
            .setOnTouchListener { v: View?, event: MotionEvent? ->
                onFloatingWidgetClick()
                false
            }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
//        if (v.getId() == R.id.close_floating_view) {//close the service and remove the from from the window
//            stopSelf();
//        }
    }

    /*  Reset position of Floating Widget view on dragging  */
    private fun resetPosition(x_cord_now: Int) {
        //Variable to check if the Floating widget view is on left side or in right side
        // initially we are displaying Floating widget view to Left side so set it to true
        var isLeft = true
        if (x_cord_now <= szWindow.x / 2) {
            isLeft = true
            moveToLeft(x_cord_now)
        } else {
            isLeft = false
            moveToRight(x_cord_now)
        }
    }

    /*  Method to move the Floating widget view to Left  */
    private fun moveToLeft(current_x_cord: Int) {
        val x = szWindow.x - current_x_cord
        object : CountDownTimer(500, 5) {
            //get params of Floating Widget view
            val mParams = mFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
            override fun onTick(t: Long) {
                val step = (500 - t) / 5
                mParams.x = (-(current_x_cord * current_x_cord * step)).toInt()

                //If you want bounce effect uncomment below line and comment above line
                // mParams.x = 0 - (int) (double) bounceValue(step, x);


                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }

            override fun onFinish() {
                mParams.x = 0

                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }
        }.start()
    }

    /*  Method to move the Floating widget view to Right  */
    private fun moveToRight(current_x_cord: Int) {
        object : CountDownTimer(500, 5) {
            //get params of Floating Widget view
            val mParams = mFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
            override fun onTick(t: Long) {
                val step = (500 - t) / 5
                mParams.x =
                    (szWindow.x + current_x_cord * current_x_cord * step - mFloatingWidgetView!!.width).toInt()

                //If you want bounce effect uncomment below line and comment above line
                //  mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - mFloatingWidgetView.getWidth();

                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }

            override fun onFinish() {
                mParams.x = szWindow.x - mFloatingWidgetView!!.width

                //Update window manager for Floating Widget
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, mParams)
            }
        }.start()
    }

    /*  Detect if the floating view is collapsed or expanded */
    private val isViewCollapsed: Boolean
        get() = mFloatingWidgetView == null || mFloatingWidgetView!!.findViewById<View>(R.id.collapse_view).visibility == View.VISIBLE

    /*  return status bar height on basis of device display metrics  */
    private val statusBarHeight: Int
        get() = ceil(
            (25 * applicationContext.resources.displayMetrics.density).toDouble()
        ).toInt()

    /*  Update Floating Widget view coordinates on Configuration change  */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        getWindowManagerDefaultDisplay()
        val layoutParams = mFloatingWidgetView!!.layoutParams as WindowManager.LayoutParams
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (layoutParams.y + (mFloatingWidgetView!!.height + statusBarHeight) > szWindow.y) {
                layoutParams.y = szWindow.y - (mFloatingWidgetView!!.height + statusBarHeight)
                mWindowManager!!.updateViewLayout(mFloatingWidgetView, layoutParams)
            }
            if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                resetPosition(szWindow.x)
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (layoutParams.x > szWindow.x) {
                resetPosition(szWindow.x)
            }
        }
    }

    /*  on Floating widget click show expanded view  */
    private fun onFloatingWidgetClick() {
        if (isViewCollapsed) {
            Log.d(TAG, "onFloatingWidgetClick: Pressed!")
            val telephonyService = getTeleService(this)
            if (telephonyService != null) {
                try {
                    telephonyService.silenceRinger()
                    telephonyService.endCall()
                } catch (e: Exception) {
                    Log.i(
                        TAG,
                        "onFloatingWidgetClick: Failed to manage call using ITelephony. Message: ${e.localizedMessage}"
                    )
                }
            } else {
                // handle call disconnect api level 28+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val mgr = getSystemService(TELECOM_SERVICE) as TelecomManager
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ANSWER_PHONE_CALLS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    if (!mgr.isInCall) return
                    mgr.endCall()
                } else {
                    Log.i(TAG, "onFloatingWidgetClick: This call can't be handled!")
                }
            }

            val sharedPrefUtil = SharedPrefUtil(this)
            val number = sharedPrefUtil.getIncomingNumber()
            val token = sharedPrefUtil.getUserToken()

            Log.d(TAG, "onFloatingWidgetClick: Mobile Number: $number")

            val mDataBuilder = Data.Builder()
                .putString("rejected_number", "$number")
                .putString("access_token", "$token")
                .build()

            val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val rejectedWorkTask = OneTimeWorkRequestBuilder<WorkerTask>()
                .setConstraints(constraint)
                .setInputData(mDataBuilder)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(rejectedWorkTask)
        }
    }

    private fun getTeleService(context: Context): ITelephony? {
        val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        try {
            val telephonyMethod =
                Class.forName(tm.javaClass.name).getDeclaredMethod("getITelephony")
            telephonyMethod.isAccessible = true
            return telephonyMethod.invoke(tm) as ITelephony
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        /*  on destroy remove both view from window manager */
        if (mFloatingWidgetView != null) mWindowManager!!.removeView(
            mFloatingWidgetView
        )
        if (removeFloatingWidgetView != null) mWindowManager!!.removeView(removeFloatingWidgetView)
    }

    companion object {
        private const val TAG = "FloatingWidgetService"
        private const val NOTIFICATION_ID = 127
    }
}