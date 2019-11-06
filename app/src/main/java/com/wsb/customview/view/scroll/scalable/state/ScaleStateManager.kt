package com.wsb.customview.view.scroll.scalable.state

import android.animation.ObjectAnimator
import android.view.MotionEvent

class ScaleStateManager constructor(scrollCallback: ScrollCallback) : BaseScaleState(scrollCallback) {

    override fun onScroll(offsetX: Float, offsetY: Float) {
        scaleState.onScroll(offsetX, offsetY)
    }

    override fun onViewDoubleTap(scaleAnimator: ObjectAnimator, motionEvent: MotionEvent) {
        scaleState.onViewDoubleTap(scaleAnimator, motionEvent)
    }

    fun changeState() {
        scaleState = if (scaleState is ScaledState) {
            unScaleState
        } else {
            scaledState
        }
    }

    override fun onViewFling(velocityX: Float, velocityY: Float) {
        scaleState.onViewFling(velocityX, velocityY)
    }


    private var unScaleState: BaseScaleState = BaseScaleState(scrollCallback)
    private var scaledState: BaseScaleState = ScaledState(scrollCallback)
    private var scaleState: BaseScaleState = unScaleState


}