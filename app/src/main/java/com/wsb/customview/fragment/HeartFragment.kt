package com.wsb.customview.fragment

import android.view.View
import com.wsb.customview.R
import com.wsb.customview.view.HeartWaveView

class HeartFragment :BaseFragment(){
    override fun initView(v: View?) {
        v?.run {
            findViewById<HeartWaveView>(R.id.hwv).startAnimator()
        }
    }

    override fun getFragmentLayout(): Int = R.layout.heart_ware

}