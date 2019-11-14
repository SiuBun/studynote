package com.wsb.customview.view.floating;

import android.view.MotionEvent;

/**
 * 拖拽处理模式
 *
 * @author wsb
 */
public class DragTouchMode extends NormalTouchMode {
    private DragTouchCallback mDragTouchCallback;

    DragTouchMode(DragTouchCallback dragTouchCallback) {
        super(null);
        mDragTouchCallback = dragTouchCallback;
    }

    @Override
    public boolean onTouchResult() {
        return true;
    }

    @Override
    public void onActionMove(MotionEvent event) {
        mDragTouchCallback.onActionMoveWhenDragMode(event);
    }

    @Override
    public void onActionUp(MotionEvent event) {
        mDragTouchCallback.onActionUpWhenDragMode(event);
    }

    /**
     * 拖拽模式下的触摸事件回调
     *
     * @author wsb
     */
    interface DragTouchCallback {

        /**
         * 拖拽模式的移动事件处理回调
         *
         * @param event 事件对象
         */
        void onActionMoveWhenDragMode(MotionEvent event);

        /**
         * 拖拽模式的抬起事件处理回调
         *
         * @param event 事件对象
         */
        void onActionUpWhenDragMode(MotionEvent event);
    }
}
