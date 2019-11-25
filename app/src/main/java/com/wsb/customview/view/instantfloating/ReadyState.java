package com.wsb.customview.view.instantfloating;

/**
 * 当前控件处于就绪等待事件状态
 *
 * @author wsb
 * */
class ReadyState extends UnConsumeTouchMode implements DisplayState{
    @Override
    public boolean touchResult() {
        return consumeTouch();
    }
}
