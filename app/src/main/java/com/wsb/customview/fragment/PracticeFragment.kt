package com.wsb.customview.fragment

import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.wsb.customview.PageModel
import com.wsb.customview.R

class PracticeFragment :BaseFragment(){

    private var pageList: MutableList<Fragment> = mutableListOf<Fragment>().apply {
        add(RaDarFragment())
        add(HeartFragment())
        add(ProgressFragment())
        add(TextMeasureFragment())
    }

    private var pageName: Array<String> = arrayOf("radar","heart","progress","textmeasure")

    private lateinit var tabLayout:TabLayout
    private lateinit var vp:ViewPager



    override fun getFragmentLayout(): Int {
        return R.layout.fragment
    }

    override fun initView(v: View?) {
        v?.run {
            tabLayout = findViewById(R.id.tab_main)
            vp = findViewById(R.id.vp)


            vp.adapter = object :FragmentPagerAdapter(childFragmentManager){
                override fun getItem(p0: Int): Fragment {
                    return pageList[p0]
                }

                override fun getCount(): Int {
                    return pageList.size
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return pageName[position]
                }
            }

            tabLayout.setupWithViewPager(vp,false)

        }
    }

    companion object{
        @JvmStatic
        fun newInstance():PracticeFragment = PracticeFragment()
    }


}