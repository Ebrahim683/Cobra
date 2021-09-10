package com.rexoit.cobra

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.utils.CallLogger
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ActivityCompat.startActivityForResult

import android.os.Build
import android.provider.Settings


private const val TAG = "MainActivity"
private const val REQUEST_CODE = 8077
private const val DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 8079

class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

    override fun onStart() {
        super.onStart()

        // request runtime permissions
        phoneCallStatePermission()

        // active this week when activity start
        thisWeek()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(this, intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE, null)
        }
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

        //NavigationView
        toolbar = findViewById(R.id.tool_bar_id)
        setSupportActionBar(toolbar)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout_id,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawer_layout_id.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //Button's Click Handler
        this_week_button.setOnClickListener {
            thisWeek()
            //For opening Number Details Page (remove this code
            startActivity(Intent(this, NumberDetailsPage::class.java))
        }

        this_month_button.setOnClickListener {
            thisMonth()
        }

        all_time_button.setOnClickListener {
            allTime()
        }

        val callLogs = CallLogger.getCallDetails(applicationContext)
        Log.d(TAG, "onCreate: ${callLogs.toString()}")
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "NewApi")
    fun thisWeek() {

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

        //Button's Background
        all_time_button.background = getDrawable(R.drawable.btn_select_bg)
        this_month_button.background = getDrawable(R.drawable.btn_unselect_bg)
        this_week_button.background = getDrawable(R.drawable.btn_unselect_bg)

        //Button's Text Color
        all_time_button.setTextColor(resources.getColor(R.color.white))
        this_month_button.setTextColor(resources.getColor(R.color.grey_color))
        this_week_button.setTextColor(resources.getColor(R.color.grey_color))

    }

    private fun phoneCallStatePermission() {
        val permissions = arrayListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_PHONE_STATE)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_CALL_LOG)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_CONTACTS)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                REQUEST_CODE
            )
        }
    }
}



