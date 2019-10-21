package com.wsb.customview.fragment

import android.view.View
import android.widget.TextView
import com.wsb.customview.view.PieView
import com.wsb.customview.R

class SectorAndPieFragment : BaseFragment() {
    override fun initView(v: View?) {
        fragmentViewGroup.addView(TextView(context).apply { text = "SectorAndPieFragment" })
//        fragmentViewGroup.addView(SecorView(context))
        fragmentViewGroup.addView(PieView(context))
    }

    override fun getFragmentLayout(): Int {
        return R.layout.frag_view
    }

    companion object{
        @JvmStatic
        fun getInstance(): SectorAndPieFragment = SectorAndPieFragment()
    }
}
