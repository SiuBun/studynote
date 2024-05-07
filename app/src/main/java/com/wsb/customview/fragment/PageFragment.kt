package com.wsb.customview.fragment

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import com.wsb.customview.PageModel
import com.wsb.customview.R

class PageFragment : BaseFragment() {
    private var practiceLayoutRes: Int = 0
    private var sampleLayoutRes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            practiceLayoutRes = it.getInt(ARG_PARAM1)
            sampleLayoutRes = it.getInt(ARG_PARAM2)
        }
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_page
    }

    override fun initView(v: View?) {
        v?.apply {
            val practiceStub = findViewById<ViewStub>(R.id.practiceStub)
            practiceStub.layoutResource = practiceLayoutRes
            practiceStub.inflate()

            val sampleStub = findViewById<ViewStub>(R.id.sampleStub)
            sampleStub.layoutResource = sampleLayoutRes
            sampleStub.inflate()

        }

    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        fun newFragment(param1: Int = 0, param2: Int = 0) =
                PageFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, param1)
                        putInt(ARG_PARAM2, param2)
                    }
                }
    }
}