package com.rexoit.cobra.ui.loading.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rexoit.cobra.ui.loading.fragments.FirstFragment
import com.rexoit.cobra.ui.loading.fragments.FourthFragment
import com.rexoit.cobra.ui.loading.fragments.SecondFragment
import com.rexoit.cobra.ui.loading.fragments.ThirdFragment


class LiquidPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> return FirstFragment()
            1 -> return SecondFragment()
            2 -> return ThirdFragment()
            3 -> return FourthFragment()
        }
        return FirstFragment()
    }

}