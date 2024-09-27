package com.wsb.customview

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MultiPagerAdapter(fragment: Fragment, private val fragmentList: List<Fragment>) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment = fragmentList[position]

    }