package com.wsb.customview.view.instantfloating;

import android.animation.TypeEvaluator;
import android.view.WindowManager;


/**
 * 窗口布局对象操作估值类
 *
 * @author wsb
 */
public class WindowLayoutEvaluator implements TypeEvaluator<WindowManager.LayoutParams> {
    private WindowManager.LayoutParams mLayoutParams;

    WindowLayoutEvaluator() {
        mLayoutParams = FwDrawUtil.createWindowLayoutParams();
    }

    @Override
    public WindowManager.LayoutParams evaluate(float fraction, WindowManager.LayoutParams startValue, WindowManager.LayoutParams endValue) {
        float x = startValue.x + (endValue.x - startValue.x) * fraction;
        float y = startValue.y + (endValue.y - startValue.y) * fraction;

        mLayoutParams.x = (int) x;
        mLayoutParams.y = (int) y;
        return mLayoutParams;
    }
}