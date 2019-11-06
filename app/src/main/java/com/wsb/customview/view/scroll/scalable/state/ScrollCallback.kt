package com.wsb.customview.view.scroll.scalable.state

import android.view.MotionEvent

/**
 * 滑动回调接口
 *
 * @author wsb
 * */
interface ScrollCallback {

    fun scrollImage(offsetX: Float, offsetY: Float)

    fun onViewFling(velocityX: Float, velocityY: Float)

    fun onViewDoubleTapFromSmall(motionEvent: MotionEvent)
}