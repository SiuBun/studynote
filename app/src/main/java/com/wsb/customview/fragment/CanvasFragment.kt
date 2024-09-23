package com.wsb.customview.fragment

import android.graphics.Color
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.wsb.customview.PageModel
import com.wsb.customview.R

class CanvasFragment :BaseFragment(){

    private var pageList: MutableList<PageModel> = mutableListOf<PageModel>().apply {
        add(PageModel(R.layout.clip_rect,"clipRect",R.layout.initial))
        add(PageModel(R.layout.clip_path,"clipCircle",R.layout.initial))
        add(PageModel(R.layout.translate,"translate",R.layout.initial))
        add(PageModel(R.layout.rotate,"rotate",R.layout.initial))
        add(PageModel(R.layout.scale,"scale",R.layout.initial))
        add(PageModel(R.layout.skew,"skew",R.layout.initial))
    }

    private lateinit var tabLayout: TabLayout
    private lateinit var vp: androidx.viewpager.widget.ViewPager



    override fun getFragmentLayout(): Int {
        return R.layout.fragment
    }

    override fun initView(v: View?) {
        v?.run {
            tabLayout = findViewById(R.id.tab_main)
            vp = findViewById(R.id.vp)
            vp.offscreenPageLimit = 2
            vp.adapter = object : androidx.fragment.app.FragmentPagerAdapter(childFragmentManager){
                override fun getItem(p0: Int): androidx.fragment.app.Fragment {
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
        fun newInstance():CanvasFragment = CanvasFragment()
    }


}