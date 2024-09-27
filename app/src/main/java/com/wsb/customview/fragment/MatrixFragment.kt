package com.wsb.customview.fragment

import android.graphics.Color
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wsb.customview.MultiPagerAdapter
import com.wsb.customview.PageModel
import com.wsb.customview.R
import com.wsb.customview.VpAdapter
import com.wsb.customview.databinding.FragmentBinding

class MatrixFragment :BaseFragment(){

    private lateinit var binding: FragmentBinding

    private var pageList: MutableList<PageModel> = mutableListOf<PageModel>().apply {
        add(PageModel(R.layout.matrix_post,"matrix_post",R.layout.initial))
        add(PageModel(R.layout.matrix_poly,"matrix_poly",R.layout.initial))
    }


    override fun getFragmentLayout(): Int {
        return R.layout.fragment
    }

    override fun initView(v: View?) {
        v?.run {
            binding = FragmentBinding.bind(this)

            binding.vp.adapter = VpAdapter(this@MatrixFragment, pageList)

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
        fun newInstance():MatrixFragment = MatrixFragment()
    }


}