package com.wsb.customview.view.floating;

import android.content.Context;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

/**
 * 悬浮窗布局类型操作定义
 *
 * @author wsb
 */
interface LayoutType {
    /**
     * 布局侧边
     *
     * @return true右边布局,false代表左边布局
     * */
    boolean getTypeBoolean();

    /**
     * 添加菜单item到菜单容器中
     *
     * 不同的布局类型的添加顺序不同
     *
     * @param group 菜单容器
     * @param list  菜单名列表
     * @param map   保存菜单名对应的控件对象
     */
    void addMenuItem(ViewGroup group, List<String> list, Map<String, ViewGroup> map);

    /**
     * 填充悬浮窗布局
     *
     * @param group 悬浮窗布局
     * @param menuContainer 菜单容器
     * @param logo 悬浮窗icon
     * */
    void stuffWindowContent(ViewGroup group, ViewGroup menuContainer, ImageView logo);

    /**
     * 屏幕旋转时响应回调
     * */
    void onConfigurationChanged();

    /**
     * 编辑该布局类型下的悬浮窗布局参数
     *
     * @param windowLayoutParams 待编辑悬浮窗布局参数
     * @return 完成编辑的布局参数
     * */
    WindowManager.LayoutParams editWindowsLayoutParams(WindowManager.LayoutParams windowLayoutParams);

    /**
     * 构建菜单项容器
     *
     * @param context 上下文
     * @param margin 边距
     *
     * @return 菜单项容器对象
     * */
    LinearLayout buildMenuItemContainer(Context context, int margin);


    /**
     * 布局类型操作回调
     *
     * @author wsb
     * */
    interface LayoutTypeCallback{

        /**
         * 屏幕旋转时候回调
         * */
        void layoutTypeOnConfigChanged();
    }

}
