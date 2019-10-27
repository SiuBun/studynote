package com.wsb.customview.fragment.practice

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment
import com.wsb.customview.view.practice.FlodImageView

class CameraIntensifyFragment : BaseFragment() {
    override fun initView(v: View?) {
        v?.run {
            val flodImageView = findViewById<FlodImageView>(R.id.fiv)
            val canvasDegress = ObjectAnimator.ofFloat(flodImageView, "canvasDegress", 0F, 360F).apply {
                duration = 2000
            }
            val cameraXDegress = ObjectAnimator.ofFloat(flodImageView, "cameraXDegress",  45F).apply {
                duration = 1000
            }

            AnimatorSet().apply {
                playSequentially(cameraXDegress,canvasDegress)

                start()
            }

        }
    }

    override fun getFragmentLayout(): Int = R.layout.camera_intensify

}
