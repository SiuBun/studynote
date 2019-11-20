package com.wsb.customview.view.instantfloating;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsb.customview.utils.DrawUtils;

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

        @Override
        public WindowMenuView stuffMenuView(Context context, SparseArray<FloatingMenuItems> sparseArray) {
            WindowMenuView windowMenuView = super.stuffMenuView(context, sparseArray);
            windowMenuView.setType(WindowMenuView.MenuType.LEFT);
            return windowMenuView;
        }

        @Override
        public void stuffWindowContent(ViewGroup windowContent, ImageView logo, View menuView) {
            windowContent.addView(logo);
            windowContent.addView(menuView);
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

        @Override
        public WindowMenuView stuffMenuView(Context context, SparseArray<FloatingMenuItems> sparseArray) {
            WindowMenuView windowMenuView = super.stuffMenuView(context, sparseArray);
            windowMenuView.setType(WindowMenuView.MenuType.RIGHT);
            return windowMenuView;
        }

        @Override
        public void stuffWindowContent(ViewGroup windowContent, ImageView logo, View menuView) {
            LinearLayout.LayoutParams menuLayoutParams = (LinearLayout.LayoutParams) menuView.getLayoutParams();
            menuLayoutParams.leftMargin = (int) FwDrawUtil.MARGIN;
            menuLayoutParams.rightMargin = (int) FwDrawUtil.MARGIN;
            menuView.setLayoutParams(menuLayoutParams);
            windowContent.addView(menuView);
            windowContent.addView(logo);
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

    @Override
    public WindowMenuView stuffMenuView(Context context, SparseArray<FloatingMenuItems> sparseArray) {
        WindowMenuView windowMenuView = new WindowMenuView(context, sparseArray);
        windowMenuView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return windowMenuView;
    }

    /**
     * 提供一个动画用于logo释放时候回归到所属一侧
     *
     * @param target         执行动画的对象
     * @param startValue     动画起点
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
        ).setDuration(300);

        animator.addListener(floatingConfig.getDisplayAnimAdapter());
        animator.addListener(floatingConfig.getTouchAnimAdapter());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }
}
