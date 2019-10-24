package com.wsb.customview.fragment

import android.view.View
import com.wsb.customview.R
import com.wsb.customview.view.CircleProgress
import com.wsb.customview.view.HeartWaveView

class TextMeasureFragment :BaseFragment(){
    private lateinit var cpv: CircleProgress

    override fun initView(v: View?) {
        v?.run {
            cpv = findViewById<CircleProgress>(R.id.cpv).apply {
                value = 60F
            }

        }
    }

    override fun getFragmentLayout(): Int = R.layout.text_measure

}