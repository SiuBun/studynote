package com.wsb.customview.view.floating;

/**
 * 悬浮窗UI回调
 *
 * @author wsb
 * */
public interface FloatingUiCallback {
    /**
     * 收起菜单项
     * */
    void shrinkMenu();

    /**
     * 半隐藏主按钮
     * */
    void hideHalfSize();
}
