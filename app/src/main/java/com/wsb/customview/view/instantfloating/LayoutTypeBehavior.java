package com.wsb.customview.view.instantfloating;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 布局类型操作行为
 *
 * @author wsb
 */
public interface LayoutTypeBehavior {
    /**
     * 隐藏悬浮窗控件
     * <p>
     * 相对{@link #restoreSize()}方法的恢复尺寸,此处需要将当前布局隐入一半
     */
    void hideHalfSize();

    /**
     * 恢复原先布局
     * <p>
     * 相对{@link #hideHalfSize()}方法的隐藏一半,此处需要恢复原先展示尺寸样式
     */
    void restoreSize();

    /**
     * 对初始的窗口布局对象进行修饰,使之适合当前布局
     * <p>
     * 定义该布局类型下初始的窗口布局对象
     *
     * @param layoutParams 准备修饰的窗口布局对象
     * @return 对应类型的窗口布局对象
     */
    WindowManager.LayoutParams wrapperOriginLayoutParams(WindowManager.LayoutParams layoutParams);

    /**
     * 填充数据到对应的菜单容器中并返回该菜单控件
     *
     * @param context     上下文
     * @param sparseArray 数据内容
     * @return 菜单控件
     */
    WindowMenuView stuffMenuView(Context context, SparseArray<FloatingMenuItems> sparseArray);

    /**
     * 对应布局填充悬浮窗内容
     *
     * @param windowContent 悬浮窗内容
     * @param logo          logo控件
     * @param menuView      菜单控件
     */
    void stuffWindowContent(ViewGroup windowContent, ImageView logo, View menuView);
}
