package com.wsb.customview.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsb.customview.databinding.ItemLoadErrorBinding
import com.wsb.customview.databinding.ItemLoadStateBinding
import com.wsb.customview.databinding.ItemNoMoreBinding

/**
 *  分页加载底部状态适配器
 *
 *  支持四种状态：加载中、错误、无更多数据、隐藏
 *
 *  @param retryCallback 重试回调函数，当点击错误状态时触发
 *
 *  @author wangshaobin
 * */
class FooterAdapter(private val retryCallback: () -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var state: PagingFooterState = PagingFooterState.HIDDEN

    companion object {
        private const val VIEW_TYPE_LOADING = 1
        private const val VIEW_TYPE_ERROR = 2
        private const val VIEW_TYPE_NO_MORE = 3
    }

    override fun getItemCount(): Int = if (state == PagingFooterState.HIDDEN) 0 else 1

    override fun getItemViewType(position: Int): Int {
        return when (state) {
            PagingFooterState.LOADING -> VIEW_TYPE_LOADING
            PagingFooterState.ERROR -> VIEW_TYPE_ERROR
            PagingFooterState.NO_MORE -> VIEW_TYPE_NO_MORE
            PagingFooterState.HIDDEN -> throw IllegalStateException("getItemViewType should not be called when hidden")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadStateBinding.inflate(inflater, parent, false)
                LoadingViewHolder(binding.root)
            }

            VIEW_TYPE_ERROR -> {
                val binding = ItemLoadErrorBinding.inflate(inflater, parent, false)
                ErrorViewHolder(binding, retryCallback)
            }

            VIEW_TYPE_NO_MORE -> {
                val binding = ItemNoMoreBinding.inflate(inflater, parent, false)
                NoMoreViewHolder(binding.root)
            }

            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // No-op
    }

    fun setState(newState: PagingFooterState) {
        if (state == newState) {
            return // 状态未变化，无需更新
        }

        val previousState = state
        state = newState

        // 根据状态变化类型执行相应的UI更新操作
        when {
            // 从隐藏状态变为显示状态：插入新item
            previousState == PagingFooterState.HIDDEN -> {
                notifyItemInserted(0)
            }
            // 从显示状态变为隐藏状态：移除item
            previousState != PagingFooterState.HIDDEN && newState == PagingFooterState.HIDDEN -> {
                notifyItemRemoved(0)
            }
            // 显示状态之间的切换：更新现有item
            else -> {
                notifyItemChanged(0)
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ErrorViewHolder(binding: ItemLoadErrorBinding, retryCallback: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { retryCallback() }
        }
    }

    class NoMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}