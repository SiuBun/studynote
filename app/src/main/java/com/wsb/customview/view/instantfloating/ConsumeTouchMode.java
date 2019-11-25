package com.wsb.customview.view.instantfloating;

/**
 * 消费触摸事件
 *
 * @author wsb
 * */
class ConsumeTouchMode implements TouchDispatcher{
    @Override
    public boolean consumeTouch() {
        return true;
    }
}
