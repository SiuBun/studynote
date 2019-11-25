package com.wsb.customview.view.instantfloating;

/**
 * 悬浮窗的展示状态
 * <p>
 * 即等待事件或者动画执行
 *
 * @author wsb
 */
interface DisplayState {
    /**
     * 接收到触摸事件时候的反馈
     *
     * @return 是否消费事件
     */
    boolean touchResult();
}
