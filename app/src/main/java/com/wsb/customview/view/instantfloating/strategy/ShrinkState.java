package com.wsb.customview.view.instantfloating.strategy;

/**
 * 悬浮窗菜单处于收起状态
 *
 * @author wsb
 * */
public class ShrinkState extends ExpandedState {
    @Override
    public void executeStateTask(StateCallback stateCallback) {
//        收起状态会设置定时任务,如果时间没到就会
        stateCallback.startTimerTask();
    }

    @Override
    public void onLogoClick(StateCallback stateCallback) {
        stateCallback.expandWindowMenu();
    }

    @Override
    public void onReceiveTouch(StateCallback stateCallback) {
        stateCallback.stopTimerTask();
    }
}
