package com.wsb.customview.view.instantfloating;

import android.view.WindowManager;

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
     * 定义该布局类型下初始的窗口布局对象
     *
     * @param layoutParams 准备修饰的窗口布局对象
     * @return 对应类型的窗口布局对象
     */
    WindowManager.LayoutParams wrapperOriginLayoutParams(WindowManager.LayoutParams layoutParams);
}
