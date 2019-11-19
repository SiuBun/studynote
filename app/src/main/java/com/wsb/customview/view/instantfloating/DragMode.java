package com.wsb.customview.view.instantfloating;

import android.view.MotionEvent;

/**
 * 拖拽模式
 *
 * @author wsb
 * */
public class DragMode implements TouchMode{
    @Override
    public boolean onTouchResult() {
        return true;
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
