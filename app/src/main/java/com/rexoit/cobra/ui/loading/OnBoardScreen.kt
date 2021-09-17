package com.rexoit.cobra.ui.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.loading.adapter.LiquidPagerAdapter

class OnBoardScreen : AppCompatActivity() {

    private lateinit var liquidPagerAdapter: LiquidPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board_screen)

        liquidPagerAdapter = LiquidPagerAdapter(supportFragmentManager)

//        liquid_pager_id.adapter = liquidPagerAdapter

    }
}