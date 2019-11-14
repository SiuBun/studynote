package com.wsb.customview.view.floating;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsb.customview.R;
import com.wsb.customview.utils.DrawUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 悬浮窗辅助类
 *
 * @author wsb
 */
class FloatingSupport {
    /**
     * 图片宽高
     */
    static final float LOGO_SIZE = DrawUtils.dp2px(45F);

    /**
     * ICON宽高
     */
    static final float ICON_SIZE = DrawUtils.dp2px(20F);

    /**
     * ICON间距
     */
    static final float MARGIN = DrawUtils.dp2px(10F);

    /**
     * 文字大小
     */
    static final float TEXT_SIZE = DrawUtils.dp2px(10F);

    static final int SCREEN_HALF = 2;

    static final float DRAG_MIN_PX = 3;

    static final String ACCOUNT_ITEM = "AccountItem";
    static final String MSG_ITEM = "MsgItem";
    static final String COMMUNITY_ITEM = "CommunityItem";
    static final String CUSTOMER_ITEM = "CustomerItem";
    static final String ANNOUNCEMENT_ITEM = "AnnouncementItem";
    static final String HIDE_ITEM = "HideItem";

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
    static WindowManager.LayoutParams createWindowLayoutParams() {

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // 设置悬浮窗口长宽数据
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    static WindowManager.LayoutParams wrapperWindowLayoutParams(boolean rightLayout, WindowManager.LayoutParams layoutParams) {
        // 调整为靠左显示
        layoutParams.x = (int) (
                rightLayout ?
                        Resources.getSystem().getDisplayMetrics().widthPixels - LOGO_SIZE :
                        0);
        layoutParams.y = (int) ((Resources.getSystem().getDisplayMetrics().heightPixels - LOGO_SIZE) / 2);
        layoutParams.gravity = Gravity.START | Gravity.TOP;
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

    static boolean rightOfScreen(float locationX) {
        return locationX > (Resources.getSystem().getDisplayMetrics().widthPixels - FloatingSupport.LOGO_SIZE) / SCREEN_HALF;
    }


    /**
     * 确定好菜单按钮基础排序
     * <p>
     * 需要调整顺序改变该方法即可
     */
    static ArrayList<String> sortMenu() {
        ArrayList<String> list = new ArrayList<>(6);
        list.add(FloatingSupport.ACCOUNT_ITEM);
        list.add(FloatingSupport.MSG_ITEM);
        list.add(FloatingSupport.COMMUNITY_ITEM);
        list.add(FloatingSupport.CUSTOMER_ITEM);
        list.add(FloatingSupport.ANNOUNCEMENT_ITEM);
        list.add(FloatingSupport.HIDE_ITEM);

        return list;
    }
}
