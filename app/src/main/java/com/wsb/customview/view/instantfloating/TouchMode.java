package com.wsb.customview.view.instantfloating;

import android.view.MotionEvent;

/**
 * 触摸模式处理对象操作
 *
 * @author wsb
 * */
interface TouchMode {
    /**
     * touch返回结果
     *
     * @return true代表拦截消费,false不消费
     * */
    boolean onTouchResult();

    /**
     * 处理移动操作
     * @param event 事件对象
     * */
    void onActionMove(MotionEvent event);

    /**
     * 处理抬起操作
     * @param event 事件对象
     * */
    void onActionUp(MotionEvent event);


    /**
     * 处理按下操作
     * @param event 事件对象
     * */
    void onActionDown(MotionEvent event);
}