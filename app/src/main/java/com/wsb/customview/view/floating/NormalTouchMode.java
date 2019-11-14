package com.wsb.customview.view.floating;

import android.view.MotionEvent;

/**
 * 常规触摸处理模式
 *
 * @author wsb
 * */
public class NormalTouchMode implements TouchModeHandle {
    private ClickTouchCallback mClickTouchCallback;

    NormalTouchMode(ClickTouchCallback clickTouchCallback) {
        mClickTouchCallback = clickTouchCallback;
    }

    @Override
    public boolean onTouchResult() {
        return false;
    }

    @Override
    public void onActionMove(MotionEvent event) {
    }

    @Override
    public void onActionUp(MotionEvent event) {
    }

    @Override
    public void onActionDown(MotionEvent event) {
        mClickTouchCallback.onActionDownWhenClickMode(event);
    }

    /**
     * 常规模式下的处理回调
     *
     * @author wsb
     * */
    interface ClickTouchCallback{
        /**
         * 常规模式的按下事件处理回调
         *
         * @param event 事件对象
         * */
        void onActionDownWhenClickMode(MotionEvent event);
    }
}
