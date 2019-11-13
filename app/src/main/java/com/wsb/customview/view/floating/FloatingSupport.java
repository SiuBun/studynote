package com.wsb.customview.view.floating;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsb.customview.R;

import org.jetbrains.annotations.NotNull;

/**
 * 悬浮窗辅助类
 *
 * @author wsb
 * */
class FloatingSupport {

    /**
     * 创建菜单项根容器
     * */
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
}
