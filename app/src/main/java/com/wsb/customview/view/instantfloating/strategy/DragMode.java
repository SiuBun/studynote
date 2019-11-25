package com.wsb.customview.view.instantfloating.strategy;

import android.view.MotionEvent;

/**
 * 拖拽模式
 *
 * @author wsb
 * */
public class DragMode extends ConsumeTouchMode implements TouchMode{
    @Override
    public boolean onTouchResult() {
        return consumeTouch();
    }

    @Override
    public void onActionMove(MotionEvent event) {

    }

    @Override
    public void onActionUp(MotionEvent event) {

    }

    @Override
    public void onActionDown(MotionEvent event) {

    }
}
