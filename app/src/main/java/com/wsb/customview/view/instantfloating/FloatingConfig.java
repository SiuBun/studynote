package com.wsb.customview.view.instantfloating;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.wsb.customview.utils.LogUtils;

/**
 * 悬浮窗配置状态
 *
 * @author wsb
 */
class FloatingConfig {

    private PressMode mPressMode = new PressMode();
    private DragMode mDragMode = new DragMode();

    private AnimateState mAnimateState = new AnimateState();
    private ReadyState mReadyState = new ReadyState();


    /**
     * 当前触摸处理模式
     */
    private TouchMode mTouchMode;


    /**
     * 当前控件状态
     */
    private DisplayState mDisplayState;


    private AnimatorListenerAdapter mDisplayAnimAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mDisplayState = mReadyState;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            mDisplayState = mAnimateState;
        }
    };
    private AnimatorListenerAdapter mTouchAnimAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            setPressMode();
        }
    };



    FloatingConfig() {
        setPressMode();
        setReadyState();
    }


    public TouchMode getTouchMode() {
        return mTouchMode;
    }

    public void setPressMode() {
        this.mTouchMode = mPressMode;
    }

    public void setDragMode() {
        this.mTouchMode = mDragMode;
    }



    public DisplayState getDisplayState() {
        return mDisplayState;
    }

    public void setAnimateState() {
        this.mDisplayState = mAnimateState;
    }

    public void setReadyState() {
        this.mDisplayState = mReadyState;
    }

    /**
     * 动画监听对象
     * <p>
     * 悬浮窗的动画都需要添加这个监听,确保动画过程中不受其他事件影响
     *
     * @return 动画监听
     */
    AnimatorListenerAdapter getDisplayAnimAdapter() {
        return mDisplayAnimAdapter;
    }

    /**
     * 触摸状态切换
     * */
    AnimatorListenerAdapter getTouchAnimAdapter() {
        return mTouchAnimAdapter;
    }

    /**
     * 动画过程消费事件,不做响应.拖拽过程也要消费事件
     */
    boolean onTouchResult() {
        return getDisplayState().touchResult() || getTouchMode().onTouchResult();
    }
}
