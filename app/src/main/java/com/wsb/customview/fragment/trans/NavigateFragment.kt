package com.wsb.customview.fragment.trans

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.wsb.customview.R
import com.wsb.customview.databinding.FragmentNavigateBinding
import com.wsb.customview.databinding.ItemNavigateLayoutBinding
import com.wsb.customview.utils.LogUtils

/**
 * TODO 请写文件描述
 *
 * @author : siubun
 * @date : 2024/05/07
 */
class NavigateFragment : Fragment() {
    private lateinit var binding: FragmentNavigateBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNavigateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigateAdapter = NavigateAdapter()
        ViewCompat.setTransitionName(binding.launch, "launch")
        binding.launch.setOnClickListener {tv->
            val name = "navigate"
            val fragment = DetailFragment.newInstance(name)


            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                addSharedElement(tv, name)
                replace(R.id.flt_menu_container, fragment)
                addToBackStack(null)
            }
        }

        binding.rvMenu.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = navigateAdapter
            addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        }
        navigateAdapter.callback = {index,itemView ->
            LogUtils.d("NavigateFragment click $index")
            ViewCompat.setTransitionName(itemView, "navigate$index")

            requireActivity().supportFragmentManager.commit {
                val name = "navigate$index"
                val fragment = DetailFragment.newInstance(name)
                addSharedElement(itemView, itemView.transitionName)
                replace(R.id.flt_menu_container, fragment)
                addToBackStack(null)
            }
        }
    }

    class NavigateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var callback: ((Int, View) -> Unit)? = null
        var dataSource = listOf("1", "2", "3", "4", "5")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return NavigateViewHolder(ItemNavigateLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun getItemCount(): Int = dataSource.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is NavigateViewHolder) {
                holder.itemView.setOnClickListener {
                    callback?.invoke(holder.adapterPosition, it)
                }

                holder.binding.tvTitle.text = dataSource[position]
            }
        }

    }

    class NavigateViewHolder(var binding: ItemNavigateLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
