package com.rexoit.cobra

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class AppLoadingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_loading_page)
        this.window.statusBarColor = resources.getColor(R.color.white)

        Handler().postDelayed({
            try {
                val intent = Intent(this, OnBoardScreen::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Log.d("AppLoading Exception", "Error = $e")
            }
        }, 2000)

    }
}