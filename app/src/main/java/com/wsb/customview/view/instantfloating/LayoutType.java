package com.wsb.customview.view.instantfloating;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 悬浮窗布局枚举
 * <p>
 * 布局类型将影响其展示位置,展开后的样式和拖动后的视图还原
 *
 * @author wsb
 */
public enum LayoutType implements LayoutTypeBehavior {
    /**
     * 悬浮窗为左边布局
     */
    LEFT {
        @Override
        public WindowManager.LayoutParams wrapperOriginLayoutParams(WindowManager.LayoutParams layoutParams) {
            WindowManager.LayoutParams params = super.wrapperOriginLayoutParams(layoutParams);
            params.x = 0;
            return params;
        }
    },

    /**
     * 悬浮窗为右边布局
     */
    RIGHT {
        @Override
        public WindowManager.LayoutParams wrapperOriginLayoutParams(WindowManager.LayoutParams layoutParams) {
            WindowManager.LayoutParams params = super.wrapperOriginLayoutParams(layoutParams);
            params.x = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - FwDrawUtil.LOGO_SIZE);
            return params;
        }
    };

    @Override
    public void hideHalfSize() {

    }

    @Override
    public void restoreSize() {

    }

    @Override
    public WindowManager.LayoutParams wrapperOriginLayoutParams(WindowManager.LayoutParams layoutParams) {
        layoutParams.y = (int) ((Resources.getSystem().getDisplayMetrics().heightPixels - FwDrawUtil.LOGO_SIZE) / 2);
        return layoutParams;
    }

    /**
     * 提供一个动画用于logo释放时候回归到所属一侧
     *
     * @param target 执行动画的对象
     * @param startValue 动画起点
     * @param floatingConfig 配置状态对象
     * @return 动画对象
     */
    public ValueAnimator getTransAnimation(Object target, WindowManager.LayoutParams startValue, FloatingConfig floatingConfig) {
        WindowManager.LayoutParams stopValue = wrapperOriginLayoutParams(FwDrawUtil.createWindowLayoutParams());
        stopValue.y = startValue.y;

        ObjectAnimator animator = ObjectAnimator.ofObject(
                target,
                "windowLayoutParams",
                new WindowLayoutEvaluator(),
                startValue,
                stopValue
        ).setDuration(1000);

        animator.addListener(floatingConfig.getDisplayAnimAdapter());
        animator.addListener(floatingConfig.getTouchAnimAdapter());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }
}
