package com.wsb.customview.fragment.practice

import android.animation.ObjectAnimator
import android.view.View
import com.wsb.customview.view.practice.DashBoardView
import com.wsb.customview.DrawUtils
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment

class DashFragment :BaseFragment(){
    override fun initView(v: View?) {
        v?.run {
            val boardView = findViewById<DashBoardView>(R.id.dbv).apply {
                radius = DrawUtils.dp2px(130F)
                scaleLength = DrawUtils.dp2px(110F)
                value = 0F
            }
            ObjectAnimator.ofFloat(boardView, "value", 18F).apply {
                duration = 1200
                start()
            }
        }
    }

    override fun getFragmentLayout(): Int = R.layout.dash
}