package com.wsb.customview.view.instantfloating;

/**
 * 不对触摸事件进行消费拦截
 *
 * @author wsb
 * */
class UnConsumeTouchMode implements TouchDispatcher {
    @Override
    public boolean consumeTouch() {
        return false;
    }
}
