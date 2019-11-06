package com.wsb.customview.view.scroll.scalable.state

import android.animation.ObjectAnimator
import android.view.MotionEvent

/**
     * 图片处于放大状态
     * */
    class ScaledState(scrollCallback: ScrollCallback) : BaseScaleState(scrollCallback) {

        override fun onViewFling(velocityX: Float, velocityY: Float) {
            scrollCallback.onViewFling(velocityX, velocityY)
        }

        override fun onScroll(offsetX: Float, offsetY: Float) {
            scrollCallback.scrollImage(offsetX, offsetY)
        }

        override fun onViewDoubleTap(scaleAnimator: ObjectAnimator, motionEvent: MotionEvent) {
            scaleAnimator.reverse()
        }
    }