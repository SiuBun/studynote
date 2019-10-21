package com.wsb.customview.fragment

import android.view.View
import com.wsb.customview.R
import com.wsb.customview.view.ShadowMaskView

class ShadowMaskFragment : BaseFragment(){
    override fun initView(v: View?) {
        fragmentViewGroup.addView(ShadowMaskView(context))
    }

    override fun getFragmentLayout(): Int {
        return R.layout.frag_view
    }

    companion object{
        @JvmStatic
        fun getInstance(): ShadowMaskFragment = ShadowMaskFragment()
    }

}
