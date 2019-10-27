package com.wsb.customview.fragment.practice

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment
import com.wsb.customview.view.practice.FlodImageView

class CameraIntensifyFragment : BaseFragment() {
    override fun initView(v: View?) {
        v?.run {
            findViewById<FlodImageView>(R.id.fiv).apply {
                ObjectAnimator.ofFloat(this,"degress",30F,360F).apply {
                    duration = 1000
                    interpolator = DecelerateInterpolator()
//                    start()
                }
            }
        }
    }

    override fun getFragmentLayout(): Int = R.layout.camera_intensify

}
