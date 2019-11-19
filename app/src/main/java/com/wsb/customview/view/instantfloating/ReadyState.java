package com.wsb.customview.view.instantfloating;

/**
 * 当前控件处于就绪等待事件状态
 *
 * @author wsb
 * */
public class ReadyState implements DisplayState{
    @Override
    public boolean touchResult() {
        return false;
    }
}
