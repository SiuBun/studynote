package com.wsb.customview.paging

/**
 *  分页加载的底部状态
 *
 *  @author wangshaobin
 * */
enum class PagingFooterState {
    /**
     * 正在加载
     */
    LOADING,

    /**
     * 加载失败
     */
    ERROR,

    /**
     * 隐藏底部加载状态
     */
    HIDDEN,

    /**
     * 没有更多数据
     */
    NO_MORE
}