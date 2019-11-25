package com.wsb.customview.view.instantfloating;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 布局类型操作行为
 *
 * @author wsb
 */
public interface LayoutTypeBehavior {
    /**
     * 隐藏悬浮窗控件
     * <p>
     * 相对{@link #restoreSize(LogoView)}方法的恢复尺寸,此处需要将当前布局隐入一半
     * @param singleLogo
     */
    void hideHalfSize(LogoView singleLogo);

    /**
     * 恢复原先布局
     * <p>
     * 相对{@link #hideHalfSize(LogoView)}方法的隐藏一半,此处需要恢复原先展示尺寸样式
     */
    void restoreSize(LogoView singleLogo);

    /**
     * 对初始的窗口布局对象进行修饰,使之适合当前布局
     * <p>
     * 定义该布局类型下初始的窗口布局对象
     *
     * @param layoutParams 准备修饰的窗口布局对象
     * @return 对应类型的窗口布局对象
     */
    WindowManager.LayoutParams editWindowLayoutParams(WindowManager.LayoutParams layoutParams);

    /**
     * 对菜单控件进行对应布局编辑并返回该菜单控件
     *
     * @param menuView 待编辑的菜单控件
     * @return 符合当前布局对象的菜单控件
     */
    WindowMenuView editMenuView(WindowMenuView menuView);

    /**
     * 对应布局填充悬浮窗内容
     *
     * @param windowContent 悬浮窗内容
     * @param logo          logo控件
     * @param menuView      菜单控件
     */
    void stuffWindowContent(ViewGroup windowContent, ImageView logo, View menuView);

    /**
     * 布局类型对logo控件有不同的包裹设置
     *
     * @param logo 等待设置的logo控件
     * */
    void editLogoView(ImageView logo);

}
