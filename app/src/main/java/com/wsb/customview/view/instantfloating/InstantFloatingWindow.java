package com.wsb.customview.view.instantfloating;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wsb.customview.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 支持订制内容的悬浮窗
 *
 * @author wsb
 */
public class InstantFloatingWindow implements FloatingWindowBehavior {
    private final WeakReference<Activity> mReference;
    private final FwHandler mFwHandler;
    /**
     * 菜单项容器
     */
    private SparseArray<FloatingMenuItems> mMenuSparseArray;

    /**
     * 悬浮窗布局类型
     */
    private LayoutType mLayoutType;

    /**
     * 悬浮窗展开状态内容
     */
    private ViewGroup mWindowContent;

    /**
     * {@link #show()}和{@link #hide()}所执行的动画
     */
    private ObjectAnimator mDisplayAnimator;

    /**
     * 当前配置状态
     */
    private FloatingConfig mFloatingConfig;


    private float mDownXInScreen, mDownYInScreen;

    private int xOriginalLayoutParams, yOriginalLayoutParams;
    /**
     * 展开悬浮窗状态下的logo
     */
    private ImageView mLogoView;

    /**
     * 展开悬浮窗状态下的菜单
     */
    private WindowMenuView mWindowMenuView;

    /**
     * 收起悬浮窗状态下的logo,该logo负责半隐藏和复位,并接收触摸移动
     */
    private LogoView mSingleLogo;

    /**
     * 收起状态的布局参数对象,触摸移动更新该对象内容
     */
    private WindowManager.LayoutParams mSingleLogoParams;

    /**
     * 展开状态的布局参数对象,该对象向{@link #mSingleLogoParams}取值后创建展开菜单
     */
    private WindowManager.LayoutParams mWindowLayoutParams;

    /**
     * 隐藏logo尺寸及修改透明度定时任务
     */
    private Runnable mHideHalfLogoTask;


    /**
     * 执行定时任务的调度器
     */
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;

    private InstantFloatingWindow(Builder builder) {
        mReference = builder.mReference;
        mFwHandler = new FwHandler(Looper.getMainLooper());

        initialFloatingData(builder);
        initialFloatingView(builder);

        getWindowService().addView(mSingleLogo, mSingleLogoParams);
    }

    private void initialFloatingData(Builder builder) {
        mMenuSparseArray = builder.mMenuItems;
        mLayoutType = builder.mLayoutType;
        mFloatingConfig = new FloatingConfig();

    }

    private void initialFloatingView(Builder builder) {
        mWindowContent = buildWindowContent(builder);
        mWindowLayoutParams = mLayoutType.editWindowLayoutParams(FwDrawUtil.createWindowLayoutParams());

        mSingleLogo = buildSingleLogo(builder);
        mSingleLogoParams = mLayoutType.editWindowLayoutParams(FwDrawUtil.createSingleLogoLayoutParams());

        mDisplayAnimator = FwDrawUtil.getDisplayAlphaAnim(mSingleLogo, mFloatingConfig);

        applyLayoutType();
    }

    private LogoView buildSingleLogo(Builder builder) {
        LogoView logoView = new LogoView(getContext());
        logoView.setDrawable(builder.mLogoDrawable);
        logoView.setOnClickListener(v -> expandWindowMenu());
        logoView.setOnTouchListener((v, event) -> onLogoTouchEvent(event));
        return logoView;
    }

    private void expandWindowMenu() {
        stopTimerTask();

        getWindowService().removeViewImmediate(mSingleLogo);
        mWindowLayoutParams.x = mSingleLogoParams.x;
        mWindowLayoutParams.y = mSingleLogoParams.y;
        getWindowService().addView(mWindowContent, mWindowLayoutParams);
    }

    private void shrinkWindowMenu() {
        getWindowService().removeViewImmediate(mWindowContent);
        getWindowService().addView(mSingleLogo, mSingleLogoParams);

//        收起菜单以后需要设置定时任务,但是该定时任务会在特定时机被取消
        startTimerTask();
    }

    private void startTimerTask() {
        LogUtils.d("开始定时任务");
        mHideHalfLogoTask = () -> {
            LogUtils.d("倒计时时间到,执行任务");
            mLayoutType.hideHalfSize(mSingleLogo);
        };
        mFwHandler.postDelayed(mHideHalfLogoTask, TimeUnit.SECONDS.toMillis(5));
    }

    private void stopTimerTask() {
        if (null != mHideHalfLogoTask) {
            LogUtils.d("取消定时任务");
            mFwHandler.removeCallbacks(mHideHalfLogoTask);
        }
    }

    private Resources getResources() {
        return getContext().getResources();
    }

    private Context getContext() {
        return mReference.get();
    }

    /**
     * 由布局类型决定如何填充悬浮窗展示样式
     */
    private void applyLayoutType() {
        mLayoutType.editLogoView(mLogoView);
        mLayoutType.editMenuView(mWindowMenuView);
        mLayoutType.stuffWindowContent(mWindowContent, mLogoView, mWindowMenuView);
    }


    /**
     * 创建悬浮窗内容控件
     *
     * @param builder 创建参数
     * @return 窗体内容
     */
    private ViewGroup buildWindowContent(Builder builder) {
        mLogoView = createWindowLogoView(builder);

        mWindowMenuView = createWindowMenuView(builder);

        return FwDrawUtil.createWindowContent(getContext());
    }

    @NotNull
    private WindowMenuView createWindowMenuView(Builder builder) {
        WindowMenuView windowMenuView = new WindowMenuView(getContext(), mMenuSparseArray);
        windowMenuView.setOnMenuClickListener((position, title) -> {
            builder.mMenuItemsClickListener.onItemClick(position, title);
            shrinkWindowMenu();
        });
        return windowMenuView;
    }

    private ImageView createWindowLogoView(Builder builder) {
        ImageView imageView = FwDrawUtil.createLogo(getContext(), BitmapFactory.decodeResource(getResources(), builder.mLogoDrawable));
        imageView.setOnClickListener((v) -> shrinkWindowMenu());
        return imageView;
    }


    /**
     * 允许修改布局类型
     *
     * @param layoutType 更新的布局类型
     */
    private void changeLayoutType(LayoutType layoutType) {
        if (layoutType != mLayoutType) {
            mLayoutType = layoutType;
            applyLayoutType();
        }
    }

    /**
     * 动画要求提供该成员变量{@link #mSingleLogoParams}的public方法
     */
    public void setWindowLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mSingleLogoParams = layoutParams;
        getWindowService().updateViewLayout(mSingleLogo, layoutParams);
    }

    private boolean onLogoTouchEvent(MotionEvent ev) {
        if (null != ev) {
            receiveMotionEvent(ev);
        }
//        如果状态不进行消费,那么返回默认情况，防止影响子View事件的消费
        return mFloatingConfig.onTouchResult();
    }

    private void receiveMotionEvent(@NonNull MotionEvent ev) {
        LogUtils.d("receiveMotionEvent", "ev.getActionMasked(): " + ev.getActionMasked());
        float xOffset, yOffset;
        float xRaw = ev.getRawX();
        float yRaw = ev.getRawY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
//                收起状态下,未隐藏状态下触摸应取消当前准备执行的任务.如果已经是隐藏一半的状态,那就恢复原先logo尺寸和透明度.再执行后续逻辑


                mDownXInScreen = xRaw;
                mDownYInScreen = yRaw;

                xOriginalLayoutParams = mSingleLogoParams.x;
                yOriginalLayoutParams = mSingleLogoParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                xOffset = xRaw - mDownXInScreen;
                yOffset = yRaw - mDownYInScreen;
                if (!FwDrawUtil.shakeTouch(xOffset, yOffset)) {
                    mFloatingConfig.setDragMode();
                    mSingleLogoParams.x = (int) (xOriginalLayoutParams + xOffset);
                    mSingleLogoParams.y = (int) (yOriginalLayoutParams + yOffset);
                    setWindowLayoutParams(mSingleLogoParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                xOffset = xRaw - mDownXInScreen;
                yOffset = yRaw - mDownYInScreen;
                if (!FwDrawUtil.shakeTouch(xOffset, yOffset)) {
                    changeLayoutType(FwDrawUtil.rightSiteOfScreen(mSingleLogoParams.x) ? LayoutType.RIGHT : LayoutType.LEFT);
                    mLayoutType.getTransAnimationWithWm(InstantFloatingWindow.this, mSingleLogoParams, mFloatingConfig).start();
                }

                break;
            default:
                break;
        }
    }


    @Override
    public void show() {
        mDisplayAnimator.start();
    }

    @Override
    public void hide() {
        mDisplayAnimator.reverse();
    }

    @Override
    public void onDestroy() {
        getWindowService().removeViewImmediate(mSingleLogo);
    }

    /**
     * 悬浮窗必依赖操作对象
     *
     * @return 窗口操作对象
     */
    private WindowManager getWindowService() {
        return (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 悬浮窗构建对象
     * <p>
     * 提供相关set方法进行悬浮窗内容设置,最后通过{@link #build()}得到悬浮窗对象
     *
     * @author wsb
     */
    public static final class Builder {

        private WeakReference<Activity> mReference;
        /**
         * 悬浮窗logo
         */
        private int mLogoDrawable;
        /**
         * 菜单项容器
         */
        private SparseArray<FloatingMenuItems> mMenuItems;

        private LayoutType mLayoutType;

        private WindowMenuView.OnMenuClickListener mMenuItemsClickListener;

        Builder(@NonNull Activity activity) {
            mReference = new WeakReference<>(activity);
        }

        public Builder setLogo(@DrawableRes int drawable) {
            mLogoDrawable = drawable;
            return this;
        }

        public Builder setMenuItems(@NonNull SparseArray<FloatingMenuItems> sparseArray) {
            mMenuItems = sparseArray;
            return this;
        }

        public Builder setMenuItemsClickListener(@NonNull WindowMenuView.OnMenuClickListener itemsClick) {
            mMenuItemsClickListener = itemsClick;
            return this;
        }

        public Builder setLayoutType(LayoutType layoutType) {
            mLayoutType = layoutType;
            return this;
        }

        public InstantFloatingWindow build() {
            return new InstantFloatingWindow(this);
        }

    }

    /**
     * 对外提供的方法用于指定当前悬浮窗所在的activity,得到构建对象
     *
     * @param activity 当前悬浮窗所挂载的activity对象
     * @return 悬浮窗构建对象 参考{@link Builder}类
     */
    public static Builder with(@NonNull Activity activity) {
        return new Builder(activity);
    }


    static class FwHandler extends Handler {

        FwHandler(Looper looper) {
            super(looper);
        }
    }
}
