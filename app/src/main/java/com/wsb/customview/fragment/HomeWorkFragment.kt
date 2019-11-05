package com.wsb.customview.fragment

import android.view.View
import com.wsb.customview.R

class HomeWorkFragment :BaseFragment(){
    override fun initView(v: View?) {
    }

    override fun getFragmentLayout(): Int = R.layout.homework

    companion object{
        @JvmStatic
        fun newInstance(): HomeWorkFragment = HomeWorkFragment()

    }
}