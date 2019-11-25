package com.wsb.customview.view.instantfloating;

/**
 * 悬浮窗菜单处于展开状态
 *
 * @author wsb
 * */
public class ExpandedState implements StretchState{
    @Override
    public void executeStateTask(StateCallback stateCallback) {

    }

    @Override
    public void onLogoClick(StateCallback stateCallback) {
        stateCallback.shrinkWindowMenu();
    }

    @Override
    public void onReceiveTouch(StateCallback stateCallback) {

    }
}
