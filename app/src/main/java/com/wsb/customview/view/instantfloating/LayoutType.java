package com.wsb.customview.view.instantfloating;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
        public WindowManager.LayoutParams editWindowLayoutParams(WindowManager.LayoutParams layoutParams) {
            WindowManager.LayoutParams params = super.editWindowLayoutParams(layoutParams);
            params.x = 0;
            return params;
        }

        @Override
        public WindowMenuView editMenuView(WindowMenuView menuView) {
            super.editMenuView(menuView);
            FrameLayout.LayoutParams menuLayoutParams = (FrameLayout.LayoutParams) menuView.getLayoutParams();
            menuLayoutParams.rightMargin = (int) FwDrawUtil.MARGIN;
            menuLayoutParams.leftMargin = (int) FwDrawUtil.LOGO_SIZE;
            menuLayoutParams.gravity = Gravity.END;

            menuView.setLayoutParams(menuLayoutParams);
            menuView.setType(WindowMenuView.MenuType.LEFT);
            return menuView;
        }

        @Override
        public void editLogoView(ImageView logo) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) FwDrawUtil.LOGO_SIZE, (int) FwDrawUtil.LOGO_SIZE);
            params.gravity = Gravity.START;
            logo.setLayoutParams(params);
        }

        @Override
        public WindowManager.LayoutParams getHideHalfSelfStopParams(WindowManager.LayoutParams startLogoParams) {
            WindowManager.LayoutParams layoutParams = super.getHideHalfSelfStopParams(startLogoParams);
            float margin = FwDrawUtil.LOGO_SIZE / 3 * 2;

            layoutParams.x = (int) -margin;
            return layoutParams;
        }
    },

    /**
     * 悬浮窗为右边布局
     */
    RIGHT {
        @Override
        public WindowManager.LayoutParams editWindowLayoutParams(WindowManager.LayoutParams layoutParams) {
            WindowManager.LayoutParams params = super.editWindowLayoutParams(layoutParams);
            params.x = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - FwDrawUtil.LOGO_SIZE);
            return params;
        }

        @Override
        public WindowMenuView editMenuView(WindowMenuView menuView) {
            super.editMenuView(menuView);
            FrameLayout.LayoutParams menuLayoutParams = (FrameLayout.LayoutParams) menuView.getLayoutParams();
            menuLayoutParams.leftMargin = (int) FwDrawUtil.MARGIN;
            menuLayoutParams.rightMargin = (int) FwDrawUtil.LOGO_SIZE;
            menuLayoutParams.gravity = Gravity.START;

            menuView.setLayoutParams(menuLayoutParams);
            menuView.setType(WindowMenuView.MenuType.RIGHT);
            return menuView;
        }

        @Override
        public void editLogoView(ImageView logo) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) FwDrawUtil.LOGO_SIZE, (int) FwDrawUtil.LOGO_SIZE);
            params.gravity = Gravity.END;
            logo.setLayoutParams(params);
        }

        @Override
        public WindowManager.LayoutParams getHideHalfSelfStopParams(WindowManager.LayoutParams startLogoParams) {
            WindowManager.LayoutParams layoutParams = super.getHideHalfSelfStopParams(startLogoParams);
            float margin = FwDrawUtil.LOGO_SIZE / 3 * 2;
            layoutParams.x = (int) (Resources.getSystem().getDisplayMetrics().widthPixels-FwDrawUtil.LOGO_SIZE+margin);
            return layoutParams;
        }
    };


    @Override
    public void hideHalfSize() {

    }

    @Override
    public void restoreSize() {

    }

    @Override
    public WindowManager.LayoutParams getHideHalfSelfStopParams(WindowManager.LayoutParams startLogoParams) {
        WindowManager.LayoutParams layoutParams = FwDrawUtil.createSingleLogoLayoutParams();
        layoutParams.alpha = 0.5F;
        layoutParams.y = startLogoParams.y;
        return layoutParams;
    }

    @Override
    public WindowManager.LayoutParams editWindowLayoutParams(WindowManager.LayoutParams layoutParams) {
        layoutParams.y = (int) ((Resources.getSystem().getDisplayMetrics().heightPixels - FwDrawUtil.LOGO_SIZE) / 2);
        return layoutParams;
    }

    @Override
    public WindowMenuView editMenuView(WindowMenuView menuView) {
        menuView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        return menuView;
    }

    @Override
    public void stuffWindowContent(ViewGroup windowContent, ImageView logo, View menuView) {
        windowContent.removeAllViews();
        windowContent.addView(logo);
        windowContent.addView(menuView);
    }


    public ValueAnimator getTransAnimationWithWc(Object target, WindowManager.LayoutParams startValue, FloatingConfig floatingConfig) {
        WindowManager.LayoutParams stopValue = editWindowLayoutParams(FwDrawUtil.createWindowLayoutParams());
        stopValue.y = startValue.y;

        return getTransAnimation(
                target,
                new WindowLayoutEvaluator(FwDrawUtil.createWindowLayoutParams()),
                startValue,
                stopValue,
                floatingConfig);
    }

    public ValueAnimator getTransAnimationWithWm(Object target, WindowManager.LayoutParams startValue, FloatingConfig floatingConfig) {
        WindowManager.LayoutParams stopValue = editWindowLayoutParams(FwDrawUtil.createSingleLogoLayoutParams());
        stopValue.y = startValue.y;

        return getTransAnimation(
                target,
                new WindowLayoutEvaluator(FwDrawUtil.createSingleLogoLayoutParams()),
                startValue,
                stopValue,
                floatingConfig);
    }

    /**
     * 提供一个动画用于logo释放时候回归到所属一侧
     *
     * @param target         执行动画的对象
     * @param evaluator      动画估值器
     * @param startValue     动画起点
     * @param stopValue      动画终点
     * @param floatingConfig 配置状态对象
     * @return 动画对象
     */
    public ValueAnimator getTransAnimation(Object target, WindowLayoutEvaluator evaluator, WindowManager.LayoutParams startValue, WindowManager.LayoutParams stopValue, FloatingConfig floatingConfig) {
        ObjectAnimator animator = ObjectAnimator.ofObject(
                target,
                "windowLayoutParams",
                evaluator,
                startValue,
                stopValue
        ).setDuration(FwDrawUtil.ANIMATOR_DURATION);

        animator.addListener(floatingConfig.getDisplayAnimAdapter());
        animator.addListener(floatingConfig.getTouchAnimAdapter());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

}
