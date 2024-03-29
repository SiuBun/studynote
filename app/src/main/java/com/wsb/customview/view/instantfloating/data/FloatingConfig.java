package com.wsb.customview.view.instantfloating.data;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.wsb.customview.view.instantfloating.strategy.AnimateState;
import com.wsb.customview.view.instantfloating.strategy.DisplayState;
import com.wsb.customview.view.instantfloating.strategy.DragMode;
import com.wsb.customview.view.instantfloating.strategy.ExpandedState;
import com.wsb.customview.view.instantfloating.strategy.PressMode;
import com.wsb.customview.view.instantfloating.strategy.ReadyState;
import com.wsb.customview.view.instantfloating.strategy.ShrinkState;
import com.wsb.customview.view.instantfloating.strategy.StateCallback;
import com.wsb.customview.view.instantfloating.strategy.StretchState;
import com.wsb.customview.view.instantfloating.strategy.TouchMode;

/**
 * 悬浮窗配置状态
 *
 * @author wsb
 */
public class FloatingConfig {

    private PressMode mPressMode = new PressMode();
    private DragMode mDragMode = new DragMode();

    private AnimateState mAnimateState = new AnimateState();
    private ReadyState mReadyState = new ReadyState();

    private ShrinkState mShrinkState = new ShrinkState();
    private ExpandedState mExpandedState = new ExpandedState();

    /**
     * 当前触摸处理模式
     */
    private TouchMode mTouchMode;


    /**
     * 当前控件展示状态
     */
    private DisplayState mDisplayState;

    /**
     * 当前控件伸缩状态
     */
    private StretchState mStretchState;


    private AnimatorListenerAdapter mDisplayAnimAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            setReadyState();
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            setAnimateState();
        }
    };
    private AnimatorListenerAdapter mTouchAnimAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            setPressMode();
        }
    };

    public FloatingConfig() {
        setPressMode();
        setReadyState();
    }

    private TouchMode getTouchMode() {
        return mTouchMode;
    }

    private void setPressMode() {
        this.mTouchMode = mPressMode;
    }

    public void setDragMode() {
        this.mTouchMode = mDragMode;
    }

    private DisplayState getDisplayState() {
        return mDisplayState;
    }

    private void setAnimateState() {
        this.mDisplayState = mAnimateState;
    }

    private void setReadyState() {
        this.mDisplayState = mReadyState;
    }

    public StretchState getStretchState() {
        return mStretchState;
    }

    /**
     * 动画监听对象
     * <p>
     * 悬浮窗的动画都需要添加这个监听,确保动画过程中不受其他事件影响
     *
     * @return 动画监听
     */
    public AnimatorListenerAdapter getDisplayAnimAdapter() {
        return mDisplayAnimAdapter;
    }

    /**
     * 触摸状态切换
     */
    public AnimatorListenerAdapter getTouchAnimAdapter() {
        return mTouchAnimAdapter;
    }

    /**
     * 动画过程消费事件,不做响应.拖拽过程也要消费事件
     */
    public boolean onTouchResult() {
        return getDisplayState().touchResult() || getTouchMode().onTouchResult();
    }

    /**
     * 当悬浮窗菜单log被点击时候调用
     *
     * @param expandMenu see {@link #changeStretchState(boolean, StateCallback)}
     * @param stateCallback see {@link #changeStretchState(boolean, StateCallback)}
     * */
    public void onLogoClick(boolean expandMenu, StateCallback stateCallback) {
        getStretchState().onLogoClick(stateCallback);
//        执行完现有状态任务后切换状态
        changeStretchState(expandMenu, stateCallback);
    }

    /**
     * 改变伸缩状态
     * <p>
     * 改变的时候会执行新状态的状态任务
     *
     * @param expandMenu    true代表展开
     * @param stateCallback 状态回调
     */
    public void changeStretchState(boolean expandMenu, StateCallback stateCallback) {
        mStretchState = expandMenu ? mExpandedState: mShrinkState ;
        getStretchState().executeStateTask(stateCallback);
    }

    public void executeStateTask(StateCallback stateCallback) {
        getStretchState().executeStateTask(stateCallback);
    }
}
