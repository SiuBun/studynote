package com.wsb.customview.fragment.trans

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.wsb.customview.databinding.FragmentDetailBinding

/**
 * TODO 请写文件描述
 *
 * @author : siubun
 * @date : 2024/05/07
 */
class DetailFragment : Fragment() {
    companion object {
        fun newInstance(name: String): DetailFragment {
            return DetailFragment().apply {
                arguments = bundleOf(
                    "name" to name
                )
            }
        }

    }

    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = ChangeBounds()
        sharedElementReturnTransition = ChangeBounds()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("name")?.let {
        }
        ViewCompat.setTransitionName(binding.ivImg, "navigate")

        binding.ivImg.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}