package com.wsb.customview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wsb.customview.fragment.PageFragment

class VpAdapter : FragmentStateAdapter {
    constructor(activity: FragmentActivity, pageList: MutableList<PageModel>): super(activity){
        this.pageList = pageList
    }
    constructor(fragment: Fragment, pageList: MutableList<PageModel>): super(fragment){
        this.pageList = pageList
    }

    private val pageList: MutableList<PageModel>

    override fun getItemCount(): Int = pageList.size

    override fun createFragment(position: Int): Fragment =
        PageFragment.newFragment(pageList[position].practiceLayoutRes, pageList[position].sampleLayoutRes)

}