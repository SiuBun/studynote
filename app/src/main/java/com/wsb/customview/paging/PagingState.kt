package com.wsb.customview.paging

/**
 * 分页状态类
 *
 * 分页加载时记录页码和是否还有更多数据的状态。
 *
 * @property currentPage 当前页码，默认为1
 * @property hasMore 是否还有更多数据，默认为true
 *
 *
 * @author wangshaobin
 */
data class PagingState(
    val currentPage: Int = 1,
    val hasMore: Boolean = true
)