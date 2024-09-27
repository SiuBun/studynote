package com.wsb.customview.fragment

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wsb.customview.PageModel
import com.wsb.customview.MultiPagerAdapter
import com.wsb.customview.R
import com.wsb.customview.VpAdapter
import com.wsb.customview.databinding.FragmentBinding

class CameraFragment :BaseFragment(){

    private var pageList: MutableList<PageModel> = mutableListOf<PageModel>().apply {
        add(PageModel(R.layout.rotate_x,"rotateX",R.layout.camera))
        add(PageModel(R.layout.rotate_x_fix,"rotateX_Fix",R.layout.camera))
        add(PageModel(R.layout.flipboard,"flipboard",R.layout.initial))
    }

    private lateinit var binding: FragmentBinding


    override fun getFragmentLayout(): Int {
        return R.layout.fragment
    }

    override fun initView(v: View?) {
        v?.run {
            binding = FragmentBinding.bind(this)

            binding.vp.adapter = VpAdapter(this@CameraFragment,pageList)

            // 将 TabLayout 和 ViewPager2 关联起来
            TabLayoutMediator(binding.tabMain, binding.vp) { tab, position ->
                // 设置每个标签的标题
                tab.text = "标签 $position"
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

    companion object{
        @JvmStatic
        fun newInstance():CameraFragment = CameraFragment()
    }



}