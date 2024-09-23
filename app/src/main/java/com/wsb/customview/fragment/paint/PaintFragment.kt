package com.wsb.customview.fragment.paint

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wsb.customview.R
import com.wsb.customview.VpAdapter


class PaintFragment : androidx.fragment.app.Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var fragmentList = mutableListOf<androidx.fragment.app.Fragment>()
    private lateinit var tabLayout: TabLayout
    private lateinit var vp: androidx.viewpager.widget.ViewPager
    private var titles: Array<String> = arrayOf("画圆画方带渐变", "画线画弧", "扇形图饼图","混合着色","Xfermode","抖动过滤","轮廓","阴影模糊")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment, container, false)
        return view.also { initView(it) }
    }

    private fun initView(view: View) {
        fragmentList.add(RectAndRoundFragment.getInstance())
        fragmentList.add(LineAndArcFragment.getInstance())
        fragmentList.add(SectorAndPieFragment.getInstance())
        fragmentList.add(ComposeFragment.getInstance())
        fragmentList.add(XfermodeFragment.getInstance())
        fragmentList.add(DitherFilterFragment.getInstance())
        fragmentList.add(PathEffectFragment.getInstance())
        fragmentList.add(ShadowMaskFragment.getInstance())

        tabLayout = view.findViewById(R.id.tab_main)

//        for (value in titles) {
//            tabLayout.addTab(tabLayout.run { newTab().setText(value) })
//        }


        vp = view.findViewById(R.id.vp)
        vp.offscreenPageLimit = 2
        vp.adapter = VpAdapter(childFragmentManager, fragmentList, titles)

        tabLayout.setupWithViewPager(vp, false)

        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
                tab?.apply {
                    customView = getTabView(i)
                }
        }

        vp.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


    }

    private fun getTabView(i: Int): View {
        val textView = TextView(context).apply {
            text = titles[i]
            textSize = 14F
            gravity = Gravity.CENTER
        }
        textView.setTextColor(Color.parseColor("#303F9F"))
        return textView
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic
        @JvmOverloads
        fun newInstance(param1: String = "", param2: String = "") =
                PaintFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


}
