package com.wsb.customview.view.instantfloating;

import android.view.MotionEvent;

/**
 * 点击模式
 *
 * @author wsb
 * */
public class PressMode implements TouchMode {
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

    }
}
