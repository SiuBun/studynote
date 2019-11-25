package com.wsb.customview.view.instantfloating;

import android.view.MotionEvent;

/**
 * 点击模式
 *
 * @author wsb
 * */
class PressMode extends UnConsumeTouchMode implements TouchMode {
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
