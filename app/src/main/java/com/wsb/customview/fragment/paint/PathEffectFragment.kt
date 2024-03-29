package com.wsb.customview.fragment.paint

import android.view.View
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment
import com.wsb.customview.view.paint.PathEffectView

class PathEffectFragment : BaseFragment(){
    override fun initView(v: View?) {
        fragmentViewGroup.addView(PathEffectView(context))
    }

    override fun getFragmentLayout(): Int {
        return R.layout.frag_view
    }

    companion object{
        @JvmStatic
        fun getInstance(): PathEffectFragment = PathEffectFragment()
    }

}
