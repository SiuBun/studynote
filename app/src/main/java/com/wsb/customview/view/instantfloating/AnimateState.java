package com.wsb.customview.view.instantfloating;

/**
 * 当前控件正在动画状态
 *
 * @author wsb
 * */
class AnimateState extends ConsumeTouchMode implements DisplayState {

    @Override
    public boolean touchResult() {
        return consumeTouch();
    }
}
