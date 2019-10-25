package com.wsb.customview.fragment.paint

import android.view.View
import android.widget.TextView
import com.wsb.customview.view.paint.LineView
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment

class LineAndArcFragment : BaseFragment() {
    override fun initView(v: View?) {
        fragmentViewGroup.addView(TextView(context).apply { text = "LineAndArcFragment" })
        fragmentViewGroup.addView(LineView(context))
    }

    override fun getFragmentLayout(): Int {
        return R.layout.frag_view
    }


    companion object{
        @JvmStatic
        fun getInstance(): LineAndArcFragment = LineAndArcFragment()
    }
}
