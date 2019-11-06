package com.wsb.customview.fragment.practice

import android.view.View
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment

class ScalaImageFragment : BaseFragment() {
    override fun initView(v: View?) {
        v?.run {

        }
    }

    override fun getFragmentLayout(): Int = R.layout.scale_image

    companion object{
        @JvmStatic
        fun newInstance(): ScalaImageFragment = ScalaImageFragment()
    }
}
