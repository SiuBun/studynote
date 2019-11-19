package com.wsb.customview.view.instantfloating;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsb.customview.R;
import com.wsb.customview.utils.DrawUtils;

import org.jetbrains.annotations.NotNull;

/**
 * 悬浮窗绘制工具
 *
 * @author wsb
 * */
class FwDrawUtil {
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

    static final float SHAKE_VALE = DrawUtils.dp2px(4F);

    /**
     * 初始化窗口管理器和窗口参数
     *
     * @return 默认的悬浮窗布局参数
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
        layoutParams.gravity = Gravity.START | Gravity.TOP;

        return layoutParams;
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
     * 创建logo控件
     * */
    static ImageView createLogo(Context context, int logoDrawable) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new FrameLayout.LayoutParams((int) LOGO_SIZE, (int) LOGO_SIZE));
        imageView.setImageResource(logoDrawable);
        return imageView;
    }


    /**
     * 根据偏移值判断是否属于抖动行为
     *
     * @return true代表抖动行为
     * */
    static boolean shakeTouch(float xOffset, float yOffset) {
        return Math.abs(xOffset)<SHAKE_VALE && Math.abs(yOffset)<SHAKE_VALE ;
    }

    /**
     * 根据偏移值判断是否属于拖动行为
     *
     * @return true代表拖动行为
     * */
    static boolean dragTouch(float v) {
        return Math.abs(v)<LOGO_SIZE/2;
    }

    /**
     * 当前X轴上的值是否在屏幕右侧
     *
     * @return true说明在屏幕右边
     * */
    static boolean rightSiteOfScreen(float locationX) {
        return locationX > (Resources.getSystem().getDisplayMetrics().widthPixels - LOGO_SIZE) / 2;
    }

    static ObjectAnimator getDisplayAlphaAnim(InstantFloatingWindow target, FloatingConfig floatingConfig) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0F, 1F).setDuration(300);
        alpha.addListener(floatingConfig.getDisplayAnimAdapter());
        return alpha;
    }
}
