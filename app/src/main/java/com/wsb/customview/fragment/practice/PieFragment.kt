package com.wsb.customview.fragment.practice

import android.view.View
import com.wsb.customview.view.practice.PieView
import com.wsb.customview.utils.DrawUtils
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment

class PieFragment :BaseFragment(){
    override fun initView(v: View?) {
        v?.run {
            findViewById<PieView>(R.id.pv).apply {
                offsetValue = DrawUtils.dp2px(10F)
                selectIndex = 1
            }
        }
    }

    override fun getFragmentLayout(): Int = R.layout.pie

}