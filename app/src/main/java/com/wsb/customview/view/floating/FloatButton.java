package com.wsb.customview.view.floating;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsb.customview.R;
import com.wsb.customview.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 免权限悬浮窗控件
 *
 * @author zgy
 */
public class FloatButton extends FrameLayout implements FloatingUiCallback,
        NormalTouchMode.ClickTouchCallback,
        DragTouchMode.DragTouchCallback, LayoutType.LayoutTypeCallback {

    static final String TAG = "FloatButton";

    /**
     * 布局容器,被展示出来的悬浮窗控件内容
     */
    LinearLayout mWindowContentView;
    /**
     * 图标
     */
    ImageView mIvLogo;
    /**
     * 菜单子项的容器
     */
    LinearLayout mMenuContainer;

    private DefaultLayoutType mLeftLayoutType = new DefaultLayoutType(this);
    private RightLayoutType mRightLayoutType = new RightLayoutType(this);
    /**
     * 当前悬浮窗布局类型(左右)
     */
    private LayoutType mWindowLayoutType = mLeftLayoutType;

    /**
     * 是否右边,默认在左侧
     */
    private boolean mRightLayout = false;

    /**
     * 展开状态,默认收起菜单项
     */
    private boolean mExpanded = false;

    /**
     * 开始触摸的起点,相距屏幕,x和y
     */
    private float mTouchDownX, mTouchDownY;

    /**
     * 悬浮窗按钮事件监听
     */
    private FloatEventListener mFloatEventListener;

    /**
     * 悬浮窗本身的布局参数
     */
    private WindowManager.LayoutParams mWindowLayoutParams;

    /**
     * 定时间隔,秒
     */
    private final static int TIMER_INTERVAL = 3;

    /**
     * 对应菜单项是否展示
     */
    private HashMap<String, Boolean> mMenuVisible = new HashMap<>(6);

    /**
     * 对应菜单项控件对象
     */
    private HashMap<String, ViewGroup> mMenuItemMap = stuffMenuItems(getContext());

    /**
     * 菜单项排序名称
     */
    private List<String> mMenuList = FloatingSupport.sortMenu();

    /**
     * 定时器调度对象
     */
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5, r -> {
        Thread thread = new Thread(r);
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    });

    /**
     * 处理定时器任务
     */
    private Handler mUiHandler = new FloatingUiHandler(this);

    /**
     * 初始化定时任务
     */
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mUiHandler.sendMessage(mUiHandler.obtainMessage(FloatingUiHandler.HANDLER_TYPE_HALF_HIDE));
        }
    };

    /**
     * 悬浮窗起始的布局参数内容
     */
    private int xOriginalLayoutParams, yOriginalLayoutParams;

    private FloatButton(@NonNull Context context, FloatEventListener listener) {
        super(context);

        mFloatEventListener = listener;

        mWindowLayoutParams = mWindowLayoutType.editWindowsLayoutParams(FloatingSupport.createWindowLayoutParams());

        // 创建悬浮控件内容
        mWindowContentView = createWindowContentView(context);

        // 将控件内容添加到窗体管理器
        getWindowService(context).addView(mWindowContentView, mWindowLayoutParams);

    }


    /**
     * 悬浮窗必依赖操作对象
     *
     * @param context 上下文
     * @return 窗口操作对象
     */
    private WindowManager getWindowService(@NonNull Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }


    /**
     * 显示出来之前检查控件是否需要重新排列
     */
    private void checkSort() {

    }

    /**
     * 显示出来之前检查控件是否满足展示条件
     */
    private void checkVisible() {
        for (String itemName : mMenuList) {
            ViewGroup viewGroup = mMenuItemMap.get(itemName);
            if (null != viewGroup) {
                Boolean visible = mMenuVisible.get(itemName);
                boolean result = null != visible ? visible : true;
                viewGroup.setVisibility(result ? VISIBLE : GONE);
            }
        }
    }

    private TouchModeHandle clickTouchMode = new NormalTouchMode(this);
    private TouchModeHandle dragTouchMode = new DragTouchMode(this);

    /**
     * 处理图标被触摸时事件类
     */
    private TouchModeHandle touchMode = clickTouchMode;

    private boolean imageOnTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchMode = clickTouchMode;
                touchMode.onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMode = dragTouchMode;
                touchMode.onActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchMode.onActionUp(event);
                break;
            default:
                break;
        }
        return touchMode.onTouchResult();
    }

    public void setWindowLayoutParams(WindowManager.LayoutParams layoutParams) {
        if (null != mWindowContentView && null != layoutParams) {
            this.mWindowLayoutParams = layoutParams;
            getWindowService(getContext()).updateViewLayout(mWindowContentView, mWindowLayoutParams);
        }
    }

    /**
     * 屏幕旋转时，调整悬浮按钮的位置
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 主要调整悬浮按钮在右侧的情况
        mWindowLayoutType.onConfigurationChanged();
    }

    @Override
    public void shrinkMenu() {
        changeMenuShowState(false);
    }

    /**
     * Logo半隐藏（隐藏2/3）
     */
    @Override
    public void hideHalfSize() {
        if (mIvLogo != null) {

            LinearLayout.LayoutParams logoLayoutParams = (LinearLayout.LayoutParams) mIvLogo.getLayoutParams();

            float m = FloatingSupport.LOGO_SIZE / 3 * 2;


            if (mRightLayout) {
                // 右侧
                mWindowLayoutParams.x = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - FloatingSupport.LOGO_SIZE + m);
                logoLayoutParams.setMargins(0, 0, (int) -m, 0);
                mIvLogo.setLayoutParams(logoLayoutParams);

            } else {
                // 左侧
                if (logoLayoutParams.leftMargin >= 0) {
                    logoLayoutParams.setMargins((int) -m, 0, 0, 0);
                    mIvLogo.setLayoutParams(logoLayoutParams);
                }
            }

            mWindowLayoutParams.alpha = 0.7f;
            setWindowLayoutParams(mWindowLayoutParams);
        }
    }

    /**
     * Logo恢复大小
     */
    private void resetSize() {
        if (mIvLogo != null) {
            LinearLayout.LayoutParams vLayoutParams = (LinearLayout.LayoutParams) mIvLogo.getLayoutParams();

            if (mRightLayout) {
                // 右侧
                mWindowLayoutParams.x = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - FloatingSupport.LOGO_SIZE);
            } else {
                // 左侧
                mWindowLayoutParams.x = 0;
            }

            vLayoutParams.width = (int) FloatingSupport.LOGO_SIZE;
            vLayoutParams.setMargins(0, 0, 0, 0);
            mIvLogo.setLayoutParams(vLayoutParams);
            mWindowLayoutParams.alpha = 1f;
            setWindowLayoutParams(mWindowLayoutParams);
        }
    }

    /**
     * 移除倒计时任务
     */
    private void cancelCountdownTask() {
        mTimerTask.cancel();
        mScheduledThreadPoolExecutor.remove(mTimerTask);
    }

    /**
     * 定时每隔5秒检查一次，是否半隐藏悬浮按钮
     */
    private void startCountdownTask() {
        mScheduledThreadPoolExecutor.schedule(mTimerTask, TIMER_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 移除定时器调度对象
     */
    private void removeTimerTaskExecutor() {
        mScheduledThreadPoolExecutor.shutdown();
        mScheduledThreadPoolExecutor = null;
    }

    /**
     * 修改菜单项展示状态
     *
     * @param expanded true代表展开,false代表收起
     */
    private void changeMenuShowState(boolean expanded) {
        if (mMenuContainer != null && mWindowContentView != null) {
            mWindowContentView.getBackground().setAlpha(expanded ? 250 : 0);
            mMenuContainer.setVisibility(expanded ? VISIBLE : GONE);
            mExpanded = expanded;
        }
    }


    /**
     * 创建悬浮窗的内容，包含logo及菜单控件
     *
     * @param context 上下文
     * @return 悬浮窗的展示内容
     */
    private LinearLayout createWindowContentView(@NonNull Context context) {
        LinearLayout windowContent = FloatingSupport.createWindowContent(context);

        // 创建Logo图标
        mIvLogo = createLogo();
        // 菜单容器
        mMenuContainer = FloatingSupport.buildMenuContainer(getContext());

        mWindowLayoutType.addMenuItem(mMenuContainer, mMenuList, mMenuItemMap);

        mWindowLayoutType.stuffWindowContent(windowContent, mMenuContainer, mIvLogo);

        windowContent.getBackground().setAlpha(0);
        windowContent.setVisibility(GONE);

        return windowContent;
    }


    /**
     * 创建悬浮按钮Logo，并设置拖放、点击展开或收起菜单的监听器
     *
     * @return 悬浮窗展示的图片
     */
    @NonNull
    private ImageView createLogo() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams((int) FloatingSupport.LOGO_SIZE, (int) FloatingSupport.LOGO_SIZE);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.floating_logo);
        // 设置拖放监听
        imageView.setOnTouchListener((view, event) -> imageOnTouch(event));

        // 已展开，则收起
        imageView.setOnClickListener(v -> changeMenuShowState(!mExpanded));

        return imageView;
    }

    /**
     * 设置单个菜单项内容布局
     *
     * @param context      上下文
     * @param resourceText 资源文件内容
     * @param drawableRes  子项图片资源
     * @param eventCode    点击该菜单项对应的事件码
     */
    private LinearLayout setMenuItemContent(Context context, String resourceText, @DrawableRes int drawableRes, final int eventCode) {
        return setMenuItemContent(context, resourceText, drawableRes, eventCode, false);
    }

    private LinearLayout setMenuItemContent(Context context, String resourceText, @DrawableRes int drawableRes, final int eventCode, boolean useMsgView) {
        LinearLayout menuItem = createMenuItem(context, resourceText, drawableRes, useMsgView);
        menuItem.setOnClickListener(v -> {
            // 收起菜单项
            changeMenuShowState(false);
            if (mFloatEventListener != null) {
                mFloatEventListener.eventCode(eventCode);
            }
        });
        return menuItem;
    }

    /**
     * 将控件填充到菜单数据中
     *
     * @param context 上下文
     */
    private HashMap<String, ViewGroup> stuffMenuItems(@NonNull Context context) {
        HashMap<String, ViewGroup> map = new HashMap<>(6);
        map.put(FloatingSupport.ACCOUNT_ITEM, setMenuItemContent(context, "account", R.drawable.floating_account, 0));
        map.put(FloatingSupport.MSG_ITEM, setMenuItemContent(context, "msg", R.drawable.floating_msg, 1, true));
        map.put(FloatingSupport.COMMUNITY_ITEM, setMenuItemContent(context, "community", R.drawable.floating_community, 2));
        map.put(FloatingSupport.CUSTOMER_ITEM, setMenuItemContent(context, "customer", R.drawable.floating_customer, 3));
        map.put(FloatingSupport.ANNOUNCEMENT_ITEM, setMenuItemContent(context, "bulletin", R.drawable.floating_bulletin, 4));
        map.put(FloatingSupport.HIDE_ITEM, setMenuItemContent(context, "hide", R.drawable.floating_hide, -1));

        return map;
    }

    /**
     * 创建悬浮窗菜单子项
     *
     * @param context       上下文
     * @param text          文案
     * @param drawableResId 子项图片资源
     * @param useMsgView    是否使用带消息提示的控件
     */
    private LinearLayout createMenuItem(Context context, String text, @DrawableRes int drawableResId, boolean useMsgView) {
        LinearLayout menuItem = mWindowLayoutType.buildMenuItemContainer(context, (int) FloatingSupport.MARGIN);
        menuItem.addView(FloatingSupport.getMenuItemIcon(context, drawableResId, (int) FloatingSupport.ICON_SIZE, useMsgView), 0);
        menuItem.addView(FloatingSupport.getMenuItemDesc(context, text, FloatingSupport.TEXT_SIZE), 1);
        return menuItem;
    }

    /**
     * 显示悬浮按钮
     */
    public void show() {
        checkVisible();
        checkSort();
        mWindowContentView.setVisibility(VISIBLE);
        startCountdownTask();
    }

    /**
     * 关闭悬浮按钮
     */
    public void hide() {
        mWindowContentView.setVisibility(GONE);
    }

    /**
     * 销毁时候移除任务队列,移除按钮布局
     */
    private void destory() {
        cancelCountdownTask();
        removeTimerTaskExecutor();
        getWindowService(getContext()).removeView(mWindowContentView);
    }

    /**
     * 跟CP的Activity生命周期有关，显示
     */
    public void onResume() {
    }

    /**
     * 跟CP的Activity生命周期有关，隐藏
     */
    public void onPause() {
    }

    /**
     * 设置消息数量
     *
     * @param num 数量
     */
    private void setMsgNum(int num) {
        ViewGroup viewGroup = mMenuItemMap.get(FloatingSupport.MSG_ITEM);
        if (null != viewGroup) {
            MenuItemWithNumView msgView = (MenuItemWithNumView) viewGroup.getChildAt(0);
            msgView.setNum(num);
        }
    }

    /**
     * 对外提供方法用于初始化悬浮窗对象
     */
    public static FloatButton attach(Context context, FloatEventListener eventListener) {
        return FloatButtonProvider.create(context, eventListener);
    }

    public FloatButton setMsgVisible(boolean msgVisible) {
        mMenuVisible.put(FloatingSupport.MSG_ITEM, msgVisible);
        return this;
    }

    public FloatButton setCommunityVisible(boolean communityVisible) {
        mMenuVisible.put(FloatingSupport.COMMUNITY_ITEM, communityVisible);
        return this;
    }

    public FloatButton setCustomerVisible(boolean customerVisible) {
        mMenuVisible.put(FloatingSupport.CUSTOMER_ITEM, customerVisible);
        return this;
    }

    public FloatButton setAnnouncementVisible(boolean announcementVisible) {
        mMenuVisible.put(FloatingSupport.ANNOUNCEMENT_ITEM, announcementVisible);
        return this;
    }

    @Override
    public void onActionDownWhenClickMode(MotionEvent event) {
        cancelCountdownTask();
        resetSize();
        mTouchDownX = event.getRawX();
        mTouchDownY = event.getRawY();
        xOriginalLayoutParams = mWindowLayoutParams.x;
        yOriginalLayoutParams = mWindowLayoutParams.y;

        LogUtils.i("按下时候悬浮窗布局对象xy轴分别为" + xOriginalLayoutParams + "," + yOriginalLayoutParams);
        LogUtils.i("当前触摸点在xy轴坐标分别为" + mTouchDownX + "," + mTouchDownY);
    }

    @Override
    public void onActionMoveWhenDragMode(MotionEvent event) {
        changeMenuShowState(false);
        mWindowLayoutParams.x = (int) (xOriginalLayoutParams + event.getRawX() - mTouchDownX);
        mWindowLayoutParams.y = (int) (yOriginalLayoutParams + event.getRawY() - mTouchDownY);
        LogUtils.i("当前移动点在xy轴坐标分别为" + mWindowLayoutParams.x + "," + mWindowLayoutParams.y);
        setWindowLayoutParams(mWindowLayoutParams);
    }

    @Override
    public void onActionUpWhenDragMode(MotionEvent event) {
        LogUtils.d("当前抬起点在xy轴坐标分别为" + mWindowLayoutParams.x + "," + mWindowLayoutParams.y);
        if (FloatingSupport.rightSiteOfScreen(mWindowLayoutParams.x)) {
            if (mWindowLayoutType instanceof DefaultLayoutType) {
                mWindowLayoutType = mRightLayoutType;
            }
        } else {
            if (mWindowLayoutType instanceof RightLayoutType) {
                mWindowLayoutType = mLeftLayoutType;
            }
        }

        scrollToBorderByPoint();
    }

    private void scrollToBorderByPoint() {
        ObjectAnimator windowLayoutParamsAnimator = FloatingSupport.getWindowLayoutParamsAnimator(
                FloatButton.this,
                mWindowLayoutType.getTypeBoolean(),
                mWindowLayoutParams,
                getAnimatorEndLayoutParams()
        );
        windowLayoutParamsAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startCountdownTask();
            }
        });
        windowLayoutParamsAnimator.start();
    }

    @NotNull
    private WindowManager.LayoutParams getAnimatorEndLayoutParams() {
        WindowManager.LayoutParams layoutParams = mWindowLayoutType.editWindowsLayoutParams(FloatingSupport.createWindowLayoutParams());
        layoutParams.y = mWindowLayoutParams.y;
        return layoutParams;
    }


    @Override
    public void layoutTypeOnConfigChanged() {
        setWindowLayoutParams(mWindowLayoutType.editWindowsLayoutParams(mWindowLayoutParams));
    }

    /**
     * 静态内部类提供单例控件对象
     *
     * @author wsb
     */
    private static class FloatButtonProvider {
        static FloatButton create(Context context, FloatEventListener eventListener) {
            return new FloatButton(context, eventListener);
        }
    }
}
