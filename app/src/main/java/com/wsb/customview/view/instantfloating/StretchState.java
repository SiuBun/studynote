package com.wsb.customview.view.instantfloating;

/**
 * 悬浮窗控件当前展开或者收起状态
 *
 * @author wsb
 */
public interface StretchState {
    /**
     * 执行该伸缩状态下的对应操作
     *
     * @param stateCallback 状态回调对象
     */
    void executeStateTask(StateCallback stateCallback);

    /**
     * 该伸缩状态下logo被点击时候回调
     *
     * @param stateCallback 回调对象
     */
    void onLogoClick(StateCallback stateCallback);

    /**
     * 该伸缩状态下收到touch事件回调
     *
     * @param stateCallback 回调对象
     * */
    void onReceiveTouch(StateCallback stateCallback);
}
