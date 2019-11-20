package com.wsb.customview.view.instantfloating;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsb.customview.utils.LogUtils;

import java.lang.ref.WeakReference;

/**
 * 支持订制内容的悬浮窗
 *
 * @author wsb
 */
public class InstantFloatingWindow extends FrameLayout implements FloatingWindowBehavior {
    /**
     * 菜单项容器
     */
    private SparseArray<FloatingMenuItems> mMenuSparseArray;

    /**
     * 悬浮窗布局类型
     */
    private LayoutType mLayoutType;

    /**
     * 布局参数对象
     */
    private WindowManager.LayoutParams mWindowLayoutParams;

    /**
     * 悬浮窗内容
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

    InstantFloatingWindow(Builder builder) {
        super(builder.mReference.get());
        initialFloatingData(builder);
        initialFloatingView(builder);

        getWindowService().addView(this, mWindowLayoutParams);
    }

    private void initialFloatingData(Builder builder) {
        mMenuSparseArray = builder.mMenuItems;
        mLayoutType = builder.mLayoutType;
        mFloatingConfig = new FloatingConfig();

    }

    private void initialFloatingView(Builder builder) {
        mWindowContent = createWindowContent(builder);
        mDisplayAnimator = FwDrawUtil.getDisplayAlphaAnim(InstantFloatingWindow.this, mFloatingConfig);
        mWindowLayoutParams = mLayoutType.wrapperOriginLayoutParams(FwDrawUtil.createWindowLayoutParams());

//        将自身作为容器,承载悬浮窗内容
        addView(mWindowContent);
    }


    private ViewGroup createWindowContent(Builder builder) {
        LinearLayout windowContent = FwDrawUtil.createWindowContent(getContext());

        ImageView logo = FwDrawUtil.createLogo(getContext(), BitmapFactory.decodeResource(getResources(),builder.mLogoDrawable));
        WindowMenuView windowMenuView = mLayoutType.stuffMenuView(getContext(),mMenuSparseArray);
        logo.setOnClickListener((v) -> {
            LogUtils.d("logo被点击");
        });

//        logo.setBackgroundColor(Color.BLUE);
//        windowMenuView.setBackgroundColor(Color.YELLOW);
        mLayoutType.stuffWindowContent(windowContent,logo,windowMenuView);
        return windowContent;
    }

    public void changeLayoutType(LayoutType layoutType) {
        if (layoutType != mLayoutType) {
            mLayoutType = layoutType;
        }
    }

    public void setWindowLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mWindowLayoutParams = layoutParams;
        getWindowService().updateViewLayout(InstantFloatingWindow.this, layoutParams);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (null != ev) {
            receiveMotionEvent(ev);
        }
//        拦截后将不再回调该方法，所以后续事件需要在onTouchEvent中回调
        return mFloatingConfig.onTouchResult() || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != ev) {
            receiveMotionEvent(ev);
        }
//        如果状态不进行消费,那么返回默认情况，防止影响子View事件的消费
        return mFloatingConfig.onTouchResult() || super.onTouchEvent(ev);
    }

    private void receiveMotionEvent(@NonNull MotionEvent ev) {
        float xOffset,yOffset;
        float xRaw = ev.getRawX();
        float yRaw = ev.getRawY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownXInScreen = xRaw;
                mDownYInScreen = yRaw;

                xOriginalLayoutParams = mWindowLayoutParams.x;
                yOriginalLayoutParams = mWindowLayoutParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                xOffset = xRaw - mDownXInScreen;
                yOffset = yRaw - mDownYInScreen;
                if (!FwDrawUtil.shakeTouch(xOffset,yOffset)) {
                    mFloatingConfig.setDragMode();
                    mWindowLayoutParams.x = (int) (xOriginalLayoutParams + xOffset);
                    mWindowLayoutParams.y = (int) (yOriginalLayoutParams + yOffset);
                    setWindowLayoutParams(mWindowLayoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                xOffset = xRaw - mDownXInScreen;
                yOffset = yRaw - mDownYInScreen;
                if (!FwDrawUtil.shakeTouch(xOffset,yOffset)) {
                    changeLayoutType(FwDrawUtil.rightSiteOfScreen(mWindowLayoutParams.x) ? LayoutType.RIGHT : LayoutType.LEFT);
                    mLayoutType.getTransAnimation(InstantFloatingWindow.this, mWindowLayoutParams, mFloatingConfig).start();
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
        getWindowService().removeViewImmediate(InstantFloatingWindow.this);
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
}
