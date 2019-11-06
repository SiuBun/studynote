package com.wsb.customview.view.scroll.scalable.state

import android.animation.ObjectAnimator
import android.view.MotionEvent

/**
 * 图片处于缩小状态
 *
 * @author wsb
 * */
open class BaseScaleState constructor(var scrollCallback: ScrollCallback) {

    /**
     * 双击时候回调
     * */
    open fun onViewDoubleTap(scaleAnimator: ObjectAnimator, motionEvent: MotionEvent){
        scrollCallback.onViewDoubleTapFromSmall(motionEvent)
        scaleAnimator.start()
    }

    /**
     * 滑动时候回调
     * */
    open fun onScroll(offsetX: Float, offsetY: Float){

    }

    /**
     * 惯性滑行时候回调
     * */
    open fun onViewFling(velocityX: Float, velocityY: Float){

    }

}