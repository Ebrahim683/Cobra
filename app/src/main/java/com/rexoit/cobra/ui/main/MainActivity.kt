package com.rexoit.cobra.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.role.RoleManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.R
import com.rexoit.cobra.data.model.CallLogInfo
import com.rexoit.cobra.ui.main.adapter.HomePageRecyclerViewAdapter
import com.rexoit.cobra.utils.CallLogger
import com.rexoit.cobra.utils.FilterState
import com.rexoit.cobra.utils.getCurrentDayDiff
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


private const val TAG = "MainActivity"
private const val REQUEST_CODE = 8077
private const val DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 8079
private const val REQUEST_ID = 1

class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private var dialogInterface: DialogInterface? = null
    private var systemAlertDialog: AlertDialog? = null

    private var filterState: FilterState = FilterState.WEEKLY

    private lateinit var homePageRecyclerViewAdapter: HomePageRecyclerViewAdapter
    private var callLogs = mutableListOf<CallLogInfo>()
    private var queryMobileNumber: String? = null

    override fun onStart() {
        super.onStart()
        // active this week when activity start
        thisWeek()
    }

    override fun onResume() {
        super.onResume()
        // take permission for cobra bubble when call ringing
        checkPermissionForCobraBubble()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_page_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_menu_id_home_page -> {
                Snackbar.make(drawer_layout_id, "Setting", Snackbar.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: Created!")

        //NavigationView
        val toolbar = tool_bar_id
        setSupportActionBar(toolbar)

        // request runtime permissions
        phoneCallStatePermission()

        try {
            callLogs = CallLogger.getCallDetails(applicationContext)
            // todo: add these all logs to cobra database
            // manage logs from app database
        } catch (e: Exception) {
            e.printStackTrace()
        }

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout_id,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawer_layout_id.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //RecyclerView Work
        homePageRecyclerViewAdapter = HomePageRecyclerViewAdapter()

        val callLogRecyclerView = recycler_view_id
        val layoutManager = LinearLayoutManager(this@MainActivity)

        val dividerItemDecoration = DividerItemDecoration(
            callLogRecyclerView.context,
            layoutManager.orientation
        )

        callLogRecyclerView.addItemDecoration(dividerItemDecoration)

        callLogRecyclerView.apply {
            setHasFixedSize(true)
            this.layoutManager = layoutManager
            this.adapter = homePageRecyclerViewAdapter
        }

        //Button's Click Handler
        this_week_button.setOnClickListener {
            thisWeek()
        }

        this_month_button.setOnClickListener {
            thisMonth()
        }

        all_time_button.setOnClickListener {
            allTime()
        }

        //Search Number (SearchView)
        search_view_id.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(s: String?): Boolean {
                queryMobileNumber = s!!.lowercase(Locale.getDefault())
                if (queryMobileNumber!!.isNotEmpty()) {
                    getCallLogs()
                } else {
                    // get call logs based on filter
                    queryMobileNumber = null
                    getCallLogs()
                }

                return false
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "NewApi")
    fun thisWeek() {
        filterState = FilterState.WEEKLY
        getCallLogs()

        //Button's Background
        this_week_button.background = getDrawable(R.drawable.btn_select_bg)
        this_month_button.background = getDrawable(R.drawable.btn_unselect_bg)
        all_time_button.background = getDrawable(R.drawable.btn_unselect_bg)

        //Button's Text Color
        this_week_button.setTextColor(resources.getColor(R.color.white))
        all_time_button.setTextColor(resources.getColor(R.color.grey_color))
        this_month_button.setTextColor(resources.getColor(R.color.grey_color))
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "NewApi")
    private fun thisMonth() {
        filterState = FilterState.MONTHLY
        getCallLogs()

        //Button's Background
        this_month_button.background = getDrawable(R.drawable.btn_select_bg)
        this_week_button.background = getDrawable(R.drawable.btn_unselect_bg)
        all_time_button.background = getDrawable(R.drawable.btn_unselect_bg)

        //Button's Text Color
        this_month_button.setTextColor(resources.getColor(R.color.white))
        all_time_button.setTextColor(resources.getColor(R.color.grey_color))
        this_week_button.setTextColor(resources.getColor(R.color.grey_color))

    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "NewApi")
    private fun allTime() {
        filterState = FilterState.ALL
        getCallLogs()
        //Button's Background
        all_time_button.background = getDrawable(R.drawable.btn_select_bg)
        this_month_button.background = getDrawable(R.drawable.btn_unselect_bg)
        this_week_button.background = getDrawable(R.drawable.btn_unselect_bg)

        //Button's Text Color
        all_time_button.setTextColor(resources.getColor(R.color.white))
        this_month_button.setTextColor(resources.getColor(R.color.grey_color))
        this_week_button.setTextColor(resources.getColor(R.color.grey_color))

    }

    private fun getCallLogs() {
        when (filterState) {
            FilterState.ALL -> {
                // get call logs and show on recyclerview
                try {
                    val unknownCallLogs = if (queryMobileNumber != null) {
                        callLogs.filter { current ->
                            current.callerName == "Unknown Caller" && current.mobileNumber!!.contains(
                                queryMobileNumber!!
                            )
                        }
                    } else {
                        callLogs.filter { current -> current.callerName == "Unknown Caller" }
                    }

                    Log.d(TAG, "getCallLogs: All: ${unknownCallLogs.size}")

                    text_id_23.text = unknownCallLogs.size.toString()

                    // set data into recyclerview
                    homePageRecyclerViewAdapter.submitList(unknownCallLogs)
                    homePageRecyclerViewAdapter.notifyDataSetChanged()
                } catch (e: SecurityException) {
                    Log.d(TAG, "onCreate: Permissions are not allowed to perform this operation")
                }
            }
            FilterState.WEEKLY -> {
                // get call logs and show on recyclerview
                try {

                    val unknownCallLogs = if (queryMobileNumber != null) {
                        callLogs.filter { current ->
                            current.callerName == "Unknown Caller" && current.mobileNumber!!.contains(
                                queryMobileNumber!!
                            ) && current.time?.getCurrentDayDiff()!! <= 7
                        }
                    } else {
                        callLogs.filter { current ->
                            current.callerName == "Unknown Caller" && current.time?.getCurrentDayDiff()!! <= 7
                        }
                    }

                    Log.d(TAG, "getCallLogs:  Weekly: ${unknownCallLogs.size}")

                    text_id_23.text = unknownCallLogs.size.toString()

                    // set data into recyclerview
                    homePageRecyclerViewAdapter.submitList(unknownCallLogs)
                    homePageRecyclerViewAdapter.notifyDataSetChanged()
                } catch (e: SecurityException) {
                    Log.d(TAG, "onCreate: Permissions are not allowed to perform this operation")
                }
            }
            FilterState.MONTHLY -> {
                // get call logs and show on recyclerview
                try {
                    // todo: get current month length and set the value.
                    val currentMonthLength = 31

                    val unknownCallLogs = if (queryMobileNumber != null) {
                        callLogs.filter { current ->
                            current.callerName == "Unknown Caller" && current.time?.getCurrentDayDiff()!! <= 7
                        }
                        callLogs.filter { current ->
                            current.callerName == "Unknown Caller" && current.mobileNumber!!.contains(
                                queryMobileNumber!!
                            ) && current.time?.getCurrentDayDiff()!! <= currentMonthLength
                        }
                    } else {
                        callLogs.filter { current ->
                            current.callerName == "Unknown Caller" && current.time?.getCurrentDayDiff()!! <= currentMonthLength
                        }
                    }

                    Log.d(TAG, "getCallLogs: Monthly: ${unknownCallLogs.size}")

                    text_id_23.text = unknownCallLogs.size.toString()

                    // set data into recyclerview
                    homePageRecyclerViewAdapter.submitList(unknownCallLogs)
                    homePageRecyclerViewAdapter.notifyDataSetChanged()
                } catch (e: SecurityException) {
                    Log.d(TAG, "onCreate: Permissions are not allowed to perform this operation")
                }
            }
        }
    }

    private fun checkPermissionForCobraBubble() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Log.d(TAG, "checkPermissionForCobraBubble: requesting permission!")

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Alert!")
                .setMessage("Display over apps permission is required. Please allow from settings.")
                .setCancelable(false)
                .setPositiveButton(
                    "Open Settings"
                ) { dialog, _ ->
                    dialogInterface = dialog

                    val settingIntent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )

                    startActivityForResult(
                        this,
                        settingIntent,
                        DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE,
                        null
                    )
                }

            Log.d(
                TAG,
                "checkPermissionForCobraBubble: Show alert dialog: ${systemAlertDialog == null}"
            )
            if (systemAlertDialog == null) {
                systemAlertDialog = alertDialog.create()
                systemAlertDialog?.show()
            }
        }
    }

    private fun phoneCallStatePermission() {
        val permissions = arrayListOf<String>()

        // check call state permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_PHONE_STATE)
        }

        // check call log permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_CALL_LOG)
        }

        // check call log permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.MANAGE_OWN_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.MANAGE_OWN_CALLS)
            }
        }

        // check read contact permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_CONTACTS)
        }

        // check read contact permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ANSWER_PHONE_CALLS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.ANSWER_PHONE_CALLS)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                REQUEST_CODE
            )
        }

        requestRole()
    }

    private fun requestRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            startActivityForResult(this, intent, REQUEST_ID, null)
        } else {
            Log.d(TAG, "requestRole: role manager not required!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: Result received!")
            dialogInterface?.cancel()

            // recheck if not permitted!
            systemAlertDialog = null
            checkPermissionForCobraBubble()
        } else {
            Log.d(TAG, "onActivityResult: Permission denied!")
        }
    }

}