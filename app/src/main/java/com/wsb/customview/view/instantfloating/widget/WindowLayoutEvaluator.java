package com.wsb.customview.view.instantfloating.widget;

import android.animation.TypeEvaluator;
import android.view.WindowManager;


/**
 * 窗口布局对象操作估值类
 *
 * @author wsb
 */
public class WindowLayoutEvaluator implements TypeEvaluator<WindowManager.LayoutParams> {
    private WindowManager.LayoutParams mLayoutParams;

    public WindowLayoutEvaluator(WindowManager.LayoutParams layoutParams) {
        mLayoutParams = layoutParams;
    }

    @Override
    public WindowManager.LayoutParams evaluate(float fraction, WindowManager.LayoutParams startValue, WindowManager.LayoutParams endValue) {
        float x = startValue.x + (endValue.x - startValue.x) * fraction;
        float y = startValue.y + (endValue.y - startValue.y) * fraction;
        float alpha = startValue.alpha + (endValue.alpha - startValue.alpha) * fraction;

        mLayoutParams.x = (int) x;
        mLayoutParams.y = (int) y;
        mLayoutParams.alpha = alpha;
        return mLayoutParams;
    }
}