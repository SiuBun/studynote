package com.wsb.customview.fragment

import android.view.View
import com.wsb.customview.view.ComposeView
import com.wsb.customview.R

class DitherFilterFragment : BaseFragment(){
    override fun initView(v: View?) {
        fragmentViewGroup.addView(DitherFilterView(context))
    }

    override fun getFragmentLayout(): Int {
        return R.layout.frag_view
    }

    companion object{
        @JvmStatic
        fun getInstance(): DitherFilterFragment = DitherFilterFragment()
    }

}
