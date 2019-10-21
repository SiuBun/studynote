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

class CameraFragment :BaseFragment(){

    private var pageList: MutableList<PageModel> = mutableListOf<PageModel>().apply {
        add(PageModel(R.layout.rotate_x,"rotateX",R.layout.camera))
        add(PageModel(R.layout.rotate_x_fix,"rotateX_Fix",R.layout.camera))
    }

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
                    return PageFragment.newFragment(pageList[p0].practiceLayoutRes,pageList[p0].sampleLayoutRes)
                }

                override fun getCount(): Int {
                    return pageList.size
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return pageList[position].title
                }
            }

            tabLayout.setupWithViewPager(vp,false)

//            for (i in 0 until tabLayout.tabCount) {
//                val tab = tabLayout.getTabAt(i)
//                tab?.apply {
//                    customView = getTabView(i)
//                }
//            }
//
//            vp.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }
    }

    private fun getTabView(i: Int): View {
        val textView = TextView(context).apply {
            text = pageList[i].title
            textSize = 14F
            gravity = Gravity.CENTER
        }
        return textView.apply {
            setTextColor(Color.parseColor("#303F9F"))
        }
    }

    companion object{
        @JvmStatic
        fun newInstance():CameraFragment = CameraFragment()
    }


}