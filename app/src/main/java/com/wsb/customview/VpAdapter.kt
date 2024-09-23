package com.wsb.customview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VpAdapter(childFragmentManager: androidx.fragment.app.FragmentManager, private val fragmentList: MutableList<androidx.fragment.app.Fragment>, private val titles: Array<String>) : androidx.fragment.app.FragmentPagerAdapter(childFragmentManager) {

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position<titles.size) titles[position] else ""
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(p0: Int): androidx.fragment.app.Fragment {
        return fragmentList[p0]
    }

}
