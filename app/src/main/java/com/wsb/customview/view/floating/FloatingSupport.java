package com.wsb.customview.view.floating;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsb.customview.R;

import org.jetbrains.annotations.NotNull;

/**
 * 悬浮窗辅助类
 *
 * @author wsb
 */
class FloatingSupport {

    /**
     * 创建菜单项根容器
     */
    static LinearLayout buildMenuContainer(Context context) {
        LinearLayout menuContainer = new LinearLayout(context);
        menuContainer.setOrientation(LinearLayout.HORIZONTAL);
        menuContainer.setBackgroundColor(Color.parseColor("#7700FF00"));
        LinearLayout.LayoutParams menuContainerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuContainer.setLayoutParams(menuContainerLayoutParams);
        menuContainer.setVisibility(View.GONE);
        return menuContainer;
    }

    /**
     * 创建悬浮窗控件根容器
     */
    static LinearLayout createWindowContent(Context context) {
        LinearLayout windowContent = new LinearLayout(context);
        windowContent.setOrientation(LinearLayout.HORIZONTAL);
        windowContent.setGravity(Gravity.CENTER_VERTICAL);
        // 设置背景图片
        windowContent.setBackgroundResource(R.drawable.floating_bg);
        windowContent.setPadding(0, 0, 0, 0);
        windowContent.setClipChildren(false);
        windowContent.setClipToPadding(false);
        return windowContent;
    }

    /**
     * 初始化窗口管理器和窗口参数
     */
    static WindowManager.LayoutParams initWindowLayoutParams(boolean rightLayoutType) {

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // 调整为靠左显示
        layoutParams.gravity = (rightLayoutType ? Gravity.END : Gravity.START) | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        layoutParams.x = 0;
        layoutParams.y = Resources.getSystem().getDisplayMetrics().heightPixels / 2;
        // 设置悬浮窗口长宽数据
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }


    /**
     * 获取菜单项文字说明
     *
     * @param context 上下文
     * @param text    菜单项说明文字
     */
    @NotNull
    static TextView getMenuItemDesc(Context context, String text, float textSize) {
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_HORIZONTAL;
        TextView textView = new TextView(context);
        textView.setLayoutParams(textParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(Color.WHITE);
        textView.setText(text);
        return textView;
    }

    /**
     * 获取菜单项图标
     *
     * @param context       上下文
     * @param drawableResId icon资源id
     * @param useMsgView    图标是否是带文字的控件
     */
    @NotNull
    static ImageView getMenuItemIcon(Context context, int drawableResId, int iconSize, boolean useMsgView) {
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconSize, iconSize);
        iconParams.gravity = Gravity.CENTER_HORIZONTAL;
        ImageView imageView = useMsgView ? new MenuItemWithNumView(context) : new ImageView(context);
        imageView.setLayoutParams(iconParams);
        imageView.setImageResource(drawableResId);
        return imageView;
    }

    /**
     * 创建菜单项容器
     *
     * @param context         上下文
     * @param marginValue     边距值
     * @param rightLayoutType 根据类型决定边距的方向
     */
    @NotNull
    static LinearLayout buildMenuItemContainer(Context context, int marginValue, boolean rightLayoutType) {
        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuParams.gravity = Gravity.CENTER_VERTICAL;
        menuParams.setMargins(rightLayoutType ? marginValue : 0, 0, rightLayoutType ? 0 : marginValue, 0);

        LinearLayout menuItem = new LinearLayout(context);
        menuItem.setLayoutParams(menuParams);
        menuItem.setOrientation(LinearLayout.VERTICAL);
        return menuItem;
    }
}
