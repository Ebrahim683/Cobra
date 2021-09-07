package com.rexoit.cobra

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //NavigationView
        toolbar = findViewById(R.id.toolBarID)
        setSupportActionBar(toolbar)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayoutID,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayoutID.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        //Button's Click Handler
        thisWeekButton.setOnClickListener {
            thisWeek()
        }

        thisMonthButton.setOnClickListener {
            thisMonth()
        }

        allTimeButton.setOnClickListener {
            allTime()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_page_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingMenuIdHomePage -> {
                Snackbar.make(drawerLayoutID, "Setting", Snackbar.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "NewApi")
    fun thisWeek() {
        //Button's Background
        thisWeekButton.background = getDrawable(R.drawable.btn_select_bg)
        thisMonthButton.background = getDrawable(R.drawable.btn_unselect_bg)
        allTimeButton.background = getDrawable(R.drawable.btn_unselect_bg)
        //Button's Text Color
        thisWeekButton.setTextColor(resources.getColor(R.color.white))
        allTimeButton.setTextColor(resources.getColor(R.color.grey_color))
        thisMonthButton.setTextColor(resources.getColor(R.color.grey_color))

        //For opening Number Details Page (remove this code
        startActivity(Intent(this,NumberDetailsPage::class.java))

    }


    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "NewApi")
    private fun thisMonth() {
        //Button's Background
        thisMonthButton.background = getDrawable(R.drawable.btn_select_bg)
        thisWeekButton.background = getDrawable(R.drawable.btn_unselect_bg)
        allTimeButton.background = getDrawable(R.drawable.btn_unselect_bg)
        //Button's Text Color
        thisMonthButton.setTextColor(resources.getColor(R.color.white))
        allTimeButton.setTextColor(resources.getColor(R.color.grey_color))
        thisWeekButton.setTextColor(resources.getColor(R.color.grey_color))
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "NewApi")
    private fun allTime() {
        //Button's Background
        allTimeButton.background = getDrawable(R.drawable.btn_select_bg)
        thisMonthButton.background = getDrawable(R.drawable.btn_unselect_bg)
        thisWeekButton.background = getDrawable(R.drawable.btn_unselect_bg)
        //Button's Text Color
        allTimeButton.setTextColor(resources.getColor(R.color.white))
        thisMonthButton.setTextColor(resources.getColor(R.color.grey_color))
        thisWeekButton.setTextColor(resources.getColor(R.color.grey_color))
    }

}



