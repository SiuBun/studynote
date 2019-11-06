package com.wsb.customview.view.state

import android.animation.ObjectAnimator

class ScaleStateManager constructor(scrollCallback: ScrollCallback) : ScaleState(scrollCallback) {

    override fun onScroll(offsetX: Float, offsetY: Float) {
        scaleState.onScroll(offsetX, offsetY)
    }

    override fun onViewDoubleTap(scaleAnimator: ObjectAnimator) {
        scaleState.onViewDoubleTap(scaleAnimator)
    }

    fun changeState() {
        scaleState = if (scaleState is UnscaleState) {
            scaledState
        } else {
            unScaleState
        }
    }

    override fun onViewFling(velocityX: Float, velocityY: Float) {
        scaleState.onViewFling(velocityX, velocityY)
    }

    private var unScaleState: ScaleState = UnscaleState(scrollCallback)
    private var scaledState: ScaleState = ScaledState(scrollCallback)
    private var scaleState: ScaleState = unScaleState

    /**
     * 图片处于缩小状态
     * */
    class UnscaleState(scrollCallback: ScrollCallback) : ScaleState(scrollCallback) {
        override fun onViewFling(velocityX: Float, velocityY: Float) {

        }

        override fun onScroll(offsetX: Float, offsetY: Float) {

        }

        override fun onViewDoubleTap(scaleAnimator: ObjectAnimator) {
            scaleAnimator.start()
        }
    }

    /**
     * 图片处于放大状态
     * */
    class ScaledState(scrollCallback: ScrollCallback) : ScaleState(scrollCallback) {
        override fun onViewFling(velocityX: Float, velocityY: Float) {
            scrollCallback.onViewFling(velocityX, velocityY)
        }

        override fun onScroll(offsetX: Float, offsetY: Float) {
            scrollCallback.scrollImage(offsetX, offsetY)
        }

        override fun onViewDoubleTap(scaleAnimator: ObjectAnimator) {
            scaleAnimator.reverse()
        }
    }

    /**
     * 滑动回调接口
     * */
    interface ScrollCallback {

        fun scrollImage(offsetX: Float, offsetY: Float)

        fun onViewFling(velocityX: Float, velocityY: Float)
    }
}