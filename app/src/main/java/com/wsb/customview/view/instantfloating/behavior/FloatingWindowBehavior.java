package com.wsb.customview.view.instantfloating.behavior;

/**
 * 悬浮窗对外行为
 *
 * @author wsb
 * */
public interface FloatingWindowBehavior {
    /**
     * 展示悬浮窗
     * */
    void show();

    /**
     * 隐藏悬浮窗
     * */
    void hide();

    /**
     * 界面销毁时候调用
     * */
    void onDestroy();
}
