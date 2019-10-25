package com.wsb.customview.fragment.practice

import android.view.View
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment
import com.wsb.customview.view.practice.CircleProgress

class TextMeasureFragment : BaseFragment(){
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