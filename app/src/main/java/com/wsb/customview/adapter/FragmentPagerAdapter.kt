package com.wsb.customview.adapter

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wsb.customview.fragment.ListFragment

class FragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val colors = listOf(Color.RED, Color.GREEN, Color.BLUE)

    override fun getItemCount(): Int = colors.size

    override fun createFragment(position: Int): Fragment {
        return ListFragment.newInstance(colors[position])
    }
}