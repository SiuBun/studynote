package com.wsb.customview.paging

/**
 * 分页加载的UI状态类
 *
 * 用于表示当前分页加载的状态，包括数据列表、刷新状态、底部状态和分页状态。
 *
 * @param T 数据类型
 * @property items 当前页的数据列表
 * @property isRefreshing 是否正在页面刷新
 * @property footerState 底部加载状态
 * @property pagingState 分页状态，包含当前页码和是否还有更多数据
 *
 * @author wangshaobin
 *  */
data class PagingListUiState<T>(
    val items: List<T> = emptyList(),
    val isRefreshing: Boolean = false,
    val footerState: PagingFooterState = PagingFooterState.HIDDEN,
    val pagingState: PagingState = PagingState()
)