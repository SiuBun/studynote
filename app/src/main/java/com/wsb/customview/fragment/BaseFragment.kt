package com.wsb.customview.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {
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
