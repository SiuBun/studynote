package com.wsb.customview.fragment.paint

import android.view.View
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment
import com.wsb.customview.view.paint.RoundView

class RectAndRoundFragment : BaseFragment() {
    override fun initView(v: View?) {
//        fragmentViewGroup.addView(RectView(context),0)
        fragmentViewGroup.addView(RoundView(context))
//        fragmentViewGroup.addView(RoundIconView(context,R.drawable.icon))
    }

    override fun getFragmentLayout(): Int {
        return R.layout.frag_view
    }

    companion object{
        @JvmStatic
        fun getInstance(): RectAndRoundFragment = RectAndRoundFragment()
    }
}
