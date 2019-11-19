package com.wsb.customview.view.instantfloating;

/**
 * 展示状态
 *
 * @author wsb
 * */
interface DisplayState {
    /**
     * 接收到触摸事件时候的反馈
     *
     * @return 是否消费事件
     * */
    boolean touchResult();
}
