package com.wsb.customview.view.instantfloating.strategy;

/**
 * 当前控件处于就绪等待事件状态
 *
 * @author wsb
 * */
public class ReadyState extends UnConsumeTouchMode implements DisplayState{
    @Override
    public boolean touchResult() {
        return consumeTouch();
    }
}
