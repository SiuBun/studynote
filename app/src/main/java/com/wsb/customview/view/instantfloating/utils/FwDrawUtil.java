package com.wsb.customview.view.instantfloating.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wsb.customview.R;
import com.wsb.customview.utils.DrawUtils;
import com.wsb.customview.view.instantfloating.data.FloatingConfig;

/**
 * 悬浮窗绘制工具
 *
 * @author wsb
 * */
public class FwDrawUtil {
    /**
     * icon和标题间隙
     * */
    public static final float ICON_TITLE_SPACING = DrawUtils.dp2px(8F);

    /**
     * 动画时长
     * */
    public static final long ANIMATOR_DURATION = 300L;

    /**
     * 图片宽高
     */
    public static final float LOGO_SIZE = DrawUtils.dp2px(45F);

    /**
     * ICON宽高
     */
    public static final float ICON_SIZE = DrawUtils.dp2px(20F);

    /**
     * ICON间距
     */
    public static final float MARGIN = DrawUtils.dp2px(10F);

    /**
     * 文字大小
     */
    public static final float TEXT_SIZE = DrawUtils.sp2px(10F);

    /**
     * 菜单项尺寸
     * */
    public static final float ITEM_SIZE = DrawUtils.dp2px(45F);

    /**
     * 手抖阈值
     * */
    private static final float SHAKE_VALE = DrawUtils.dp2px(4F);

    /**
     * 初始化窗口管理器和窗口参数
     *
     * @return 默认的悬浮窗布局参数
     */
    public static WindowManager.LayoutParams createWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // 设置悬浮窗口长宽数据
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    /**
     * 创建单独图标的布局参数
     *
     * @return 图标专用布局参数
     * */
    public static WindowManager.LayoutParams createSingleLogoLayoutParams() {
        WindowManager.LayoutParams layoutParams = createWindowLayoutParams();
        // 设置悬浮窗口长宽数据
        layoutParams.width = (int) FwDrawUtil.LOGO_SIZE;
        layoutParams.height = (int) FwDrawUtil.LOGO_SIZE;

        return layoutParams;
    }


    /**
     * 创建悬浮窗控件根容器
     */
    public static FrameLayout createWindowContent(Context context) {
        FrameLayout windowContent = new FrameLayout(context);
        // 设置背景图片
        windowContent.setBackgroundResource(R.drawable.floating_bg);
        windowContent.setPadding(0, 0, 0, 0);
        windowContent.setClipChildren(false);
        windowContent.setClipToPadding(false);
        return windowContent;
    }

    /**
     * 创建logo控件
     * */
    public static ImageView createLogo(Context context, Bitmap logoBitmap) {
        ImageView imageView = createSingleLogo(context, logoBitmap);
        imageView.setLayoutParams(new FrameLayout.LayoutParams((int) FwDrawUtil.LOGO_SIZE, (int) FwDrawUtil.LOGO_SIZE));
        return imageView;
    }

    /**
     * 创建独立的logo控件
     * */
    public static ImageView createSingleLogo(Context context, Bitmap logoBitmap) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(logoBitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }


    /**
     * 根据偏移值判断是否属于抖动行为
     *
     * @return true代表抖动行为
     * */
    public static boolean shakeTouch(float xOffset, float yOffset) {
        return Math.abs(xOffset)<SHAKE_VALE && Math.abs(yOffset)<SHAKE_VALE ;
    }

    /**
     * 根据偏移值判断是否属于拖动行为
     *
     * @return true代表拖动行为
     * */
    public static boolean dragTouch(float v) {
        return Math.abs(v)<LOGO_SIZE/2;
    }

    /**
     * 当前X轴上的值是否在屏幕右侧
     *
     * @return true说明在屏幕右边
     * */
    public static boolean rightSiteOfScreen(float locationX) {
        return locationX > (Resources.getSystem().getDisplayMetrics().widthPixels - LOGO_SIZE) / 2;
    }

    /**
     * 显示和隐藏悬浮窗动画
     *
     * @param target 执行动画的对象
     * @param floatingConfig 状态对象
     * @return 动画对象
     * */
    public static ObjectAnimator getDisplayAlphaAnim(View target, FloatingConfig floatingConfig) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(target, "alpha", 0F, 1F).setDuration(200);
        alphaAnimator.addListener(floatingConfig.getDisplayAnimAdapter());
        return alphaAnimator;
    }
}
