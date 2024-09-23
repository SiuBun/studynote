package com.wsb.customview.fragment.practice

import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.View
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment

class PracticeFragment : BaseFragment(){

    private var pageList: MutableList<androidx.fragment.app.Fragment> = mutableListOf<androidx.fragment.app.Fragment>().apply {
        add(CameraIntensifyFragment())
        add(TextMeasureFragment())
        add(ProgressFragment())
        add(PieFragment())
        add(DashFragment())
        add(RaDarFragment())
        add(HeartFragment())
    }

    private var pageName: Array<String> = arrayOf(
            "camera",
            "textmeasure",
            "progress",
            "pie",
            "dash",
            "radar",
            "heart"
    )

    private lateinit var tabLayout: TabLayout
    private lateinit var vp: androidx.viewpager.widget.ViewPager



    override fun getFragmentLayout(): Int {
        return R.layout.fragment
    }

    override fun initView(v: View?) {
        v?.run {
            tabLayout = findViewById(R.id.tab_main)
            vp = findViewById(R.id.vp)


            vp.adapter = object : androidx.fragment.app.FragmentPagerAdapter(childFragmentManager){
                override fun getItem(p0: Int): androidx.fragment.app.Fragment {
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
        fun newInstance(): PracticeFragment = PracticeFragment()
    }


}