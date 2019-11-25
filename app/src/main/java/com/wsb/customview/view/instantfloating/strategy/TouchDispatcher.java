package com.wsb.customview.view.instantfloating.strategy;

/**
 * 触摸事件分发接口
 *
 * @author wsb
 * */
interface TouchDispatcher {
    /**
     * 是否消费事件
     *
     * @return true代表消费拦截
     * */
    boolean consumeTouch();
}
