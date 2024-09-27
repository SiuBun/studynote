package com.wsb.customview.fragment

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wsb.customview.PageModel
import com.wsb.customview.R
import com.wsb.customview.VpAdapter
import com.wsb.customview.databinding.FragmentBinding

class CanvasFragment :BaseFragment(){
    private lateinit var binding: FragmentBinding

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
            binding = FragmentBinding.bind(this)

            binding.vp.adapter = VpAdapter(this@CanvasFragment, pageList)

            // 将 TabLayout 和 ViewPager2 关联起来
            TabLayoutMediator(binding.tabMain, binding.vp) { tab, position ->
                // 设置每个标签的标题
                tab.text = pageList[position].title
            }.attach()

            // 可选：设置页面切换监听器
            binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // 在这里处理页面切换事件
                }
            })

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