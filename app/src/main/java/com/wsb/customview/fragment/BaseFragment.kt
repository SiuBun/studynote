package com.wsb.customview.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

abstract class BaseFragment : androidx.fragment.app.Fragment() {
    protected lateinit var fragmentViewGroup: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getFragmentLayout(), container, false).also { v ->
            fragmentViewGroup = v as ViewGroup
            initView(v)
        }
    }

    abstract fun initView(v: View?)

    @LayoutRes
    abstract fun getFragmentLayout(): Int
}
