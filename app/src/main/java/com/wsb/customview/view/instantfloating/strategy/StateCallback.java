package com.wsb.customview.view.instantfloating.strategy;

/**
 * 悬浮窗菜单状态控制
 *
 * @author wsb
 * */
public interface StateCallback {
    /**
     * 收缩菜单
     * */
    void shrinkWindowMenu();

    /**
     * 展开菜单
     * */
    void expandWindowMenu();

    /**
     * 开启定时任务准备隐藏图标
     * */
    void startTimerTask();


    /**
     * 停止定时任务准备隐藏图标
     * */
    void stopTimerTask();

}
