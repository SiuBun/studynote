package com.wsb.customview.view.state

import android.animation.ObjectAnimator

/**
 * 缩放状态
 *
 * @author wsb
 * */
abstract class ScaleState constructor(var scrollCallback: ScaleStateManager.ScrollCallback) {

    /**
     * 双击时候回调
     * */
    abstract fun onViewDoubleTap(scaleAnimator: ObjectAnimator)

    /**
     * 滑动时候回调
     * */
    abstract fun onScroll(offsetX: Float, offsetY: Float)

    /**
     * 惯性滑行时候回调
     * */
    abstract fun onViewFling(velocityX: Float, velocityY: Float)
}