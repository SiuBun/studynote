package com.wsb.customview.fragment.paint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wsb.customview.MultiPagerAdapter
import com.wsb.customview.R
import com.wsb.customview.databinding.FragmentBinding


class PaintFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBinding
    private var fragmentList = mutableListOf<Fragment>()
    private var titles: Array<String> =
        arrayOf("画圆画方带渐变", "画线画弧", "扇形图饼图", "混合着色", "Xfermode", "抖动过滤", "轮廓", "阴影模糊")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment, container, false)
        return view.also {
            binding = FragmentBinding.bind(it)
            initView(it) }
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

//        for (value in titles) {
//            tabLayout.addTab(tabLayout.run { newTab().setText(value) })
//        }


        binding.vp.adapter = MultiPagerAdapter(this, fragmentList)

        // 将 TabLayout 和 ViewPager2 关联起来
        TabLayoutMediator(binding.tabMain, binding.vp) { tab, position ->
            // 设置每个标签的标题
            tab.text = titles[position]
        }.attach()

        // 可选：设置页面切换监听器
        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 在这里处理页面切换事件
            }
        })


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
