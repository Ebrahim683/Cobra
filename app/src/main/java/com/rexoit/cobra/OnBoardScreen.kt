package com.rexoit.cobra

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.rexoit.cobra.adapters.LiquidPagerAdapter
import kotlinx.android.synthetic.main.activity_on_board_screen.*

class OnBoardScreen : AppCompatActivity() {

    private lateinit var liquidPagerAdapter: LiquidPagerAdapter
    private val TAG = "onBoardScreen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board_screen)

        liquidPagerAdapter = LiquidPagerAdapter(supportFragmentManager)


        try {
            liquid_pager_id.apply {
                adapter = liquidPagerAdapter
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error = $e")
        }


    }
}