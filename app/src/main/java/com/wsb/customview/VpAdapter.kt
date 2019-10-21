package com.wsb.customview

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class VpAdapter(childFragmentManager: FragmentManager, private val fragmentList: MutableList<Fragment>, private val titles: Array<String>) : FragmentPagerAdapter(childFragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position<titles.size) titles[position] else ""
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(p0: Int): Fragment {
        return fragmentList[p0]
    }

}
