package com.wsb.customview.view.floating;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsb.customview.R;
import com.wsb.customview.utils.DrawUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public class FloatButton extends FrameLayout implements FloatingUiCallback {

    static final String TAG = "FloatButton";


    private static final float DRAG_MIN_PX = 3;
    private static final int SCREEN_HALF = 2;

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
    /**
     * 消息图标
     */
    MenuItemWithNumView mMsgView;

    /**
     * 是否右边,默认在左侧
     */
    private boolean mRightLayout = false;

    /**
     * 展开状态,默认收起菜单项
     */
    private boolean mExpanded = false;

    private int mScreenWidth;

    private int mScreenHeight;

    /**
     * 开始触摸的起点,x和y
     */
    private float mTouchDownX, mTouchDownY;

    private float mWindowOffsetX, mWindowOffsetY;

    /**
     * 图片宽高
     */
    private float mLogoSize = DrawUtils.dp2px(45F);

    /**
     * ICON宽高
     */
    private float mIconSize = DrawUtils.dp2px(20F);

    /**
     * ICON间距
     */
    private float mMargin = DrawUtils.dp2px(10F);

    /**
     * 文字大小
     */
    private float mTextSize = DrawUtils.dp2px(10F);

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
    private HashMap<String, ViewGroup> mMenuItemMap = new HashMap<>(6);

    /**
     * 菜单项排序名称
     */
    private List<String> mMenuList = new ArrayList<>(6);

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
    private static final String ACCOUNT_ITEM = "AccountItem";
    private static final String MSG_ITEM = "MsgItem";
    private static final String COMMUNITY_ITEM = "CommunityItem";
    private static final String CUSTOMER_ITEM = "CustomerItem";
    private static final String ANNOUNCEMENT_ITEM = "AnnouncementItem";
    private static final String HIDE_ITEM = "HideItem";


    private FloatButton(@NonNull Context context, FloatEventListener listener) {
        super(context);

        mFloatEventListener = listener;

        resetScreenSize();

        mWindowLayoutParams = initWindowLayoutParams();

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
     * 确定好菜单按钮基础排序
     * <p>
     * 需要调整顺序改变该方法即可
     */
    private void sortMenu() {
        mMenuList.add(ACCOUNT_ITEM);
        mMenuList.add(MSG_ITEM);
        mMenuList.add(COMMUNITY_ITEM);
        mMenuList.add(CUSTOMER_ITEM);
        mMenuList.add(ANNOUNCEMENT_ITEM);
        mMenuList.add(HIDE_ITEM);
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


    /**
     * 初始化窗口管理器和窗口参数
     */
    private WindowManager.LayoutParams initWindowLayoutParams() {

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // 调整为靠左显示
        layoutParams.gravity = (mRightLayout ? Gravity.END : Gravity.START) | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        layoutParams.x = 0;
        layoutParams.y = mScreenHeight / 2;
        // 设置悬浮窗口长宽数据
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }


    /**
     * 处理主按钮移动事件
     */
    public boolean handleOnTouch(MotionEvent event) {

        // 排除半隐藏干扰
//        cancelCountdownTask();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

//        拖动状态
        boolean dragMode = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = (int) event.getX();
                mTouchDownY = (int) event.getY();

                // 手指按下时恢复大小
                resetSize();
                dragMode = false;
                break;

            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 移动过3个像素才算拖动，OnClick事件才屏蔽
                if (Math.abs(mTouchDownX - mMoveStartX) > DRAG_MIN_PX && Math.abs(mTouchDownY - mMoveStartY) > DRAG_MIN_PX) {
                    dragMode = true;
                    // 拖动时收起菜单项
                    changeMenuShowState(false);
                    // 拖动时恢复大小
                    resetSize();
                    mWindowLayoutParams.x = (int) (x - mTouchDownX);
                    mWindowLayoutParams.y = (int) (y - mTouchDownY);
                    getWindowService(getContext()).updateViewLayout(mWindowContentView, mWindowLayoutParams);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 移到左侧
                if (mWindowLayoutParams.x <= mScreenWidth / SCREEN_HALF) {
                    mWindowLayoutParams.x = 0;
                    mRightLayout = false;
                } else {
                    // 移到右侧
                    mWindowLayoutParams.x = mScreenWidth - mIvLogo.getMeasuredWidth();
                    mRightLayout = true;
                }
                getWindowService(getContext()).updateViewLayout(mWindowContentView, mWindowLayoutParams);
                mTouchDownX = mTouchDownY = 0;
                // 恢复定时任务
//                startCountdownTask();
                break;
            default:
                break;
        }

        // 此处必须返回false，否则OnClickListener获取不到监听
        return dragMode;
    }

    /**
     * 屏幕旋转时，调整悬浮按钮的位置
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 主要调整悬浮按钮在右侧的情况
        if (mWindowContentView != null && mRightLayout) {
            resetScreenSize();
            mWindowLayoutParams.x = mScreenWidth;
            getWindowService(getContext()).updateViewLayout(mWindowContentView, mWindowLayoutParams);
        }
    }

    /**
     * 获取当前屏幕信息赋值宽高尺寸
     */
    private void resetScreenSize() {
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
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

            float m = mLogoSize / 3 * 2;

            if (mRightLayout) {
                // 右侧
                mWindowLayoutParams.x = (int) (mScreenWidth - mLogoSize + m);
                logoLayoutParams.setMargins(0, 0, (int) -m, 0);
                mIvLogo.setLayoutParams(logoLayoutParams);

            } else {
                // 左侧
                if (logoLayoutParams.leftMargin >= 0) {
                    logoLayoutParams.setMargins((int) -m, 0, 0, 0);
                    mIvLogo.setLayoutParams(logoLayoutParams);
                }
            }

            try {
                mWindowLayoutParams.alpha = 0.7f;
                getWindowService(getContext()).updateViewLayout(mWindowContentView, mWindowLayoutParams);
            } catch (Exception ex) {
                Log.e(TAG, "WindowManager对已脱离的View操作产生异常，已捕获");
                ex.printStackTrace();
            }
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
                mWindowLayoutParams.x = (int) (mScreenWidth - mLogoSize);
            } else {
                // 左侧
                mWindowLayoutParams.x = 0;
            }

            vLayoutParams.width = (int) mLogoSize;
            vLayoutParams.setMargins(0, 0, 0, 0);
            mIvLogo.setLayoutParams(vLayoutParams);
            mWindowLayoutParams.alpha = 1f;
            getWindowService(getContext()).updateViewLayout(mWindowContentView, mWindowLayoutParams);
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
     * 销毁时候移除任务队列,移除按钮布局
     */
    private void destory() {
//        cancelCountdownTask();
        removeTimerTaskExecutor();
        getWindowService(getContext()).removeView(mWindowContentView);
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

        sortMenu();
        stuffMenuItems(context);

        // 创建Logo图标
        mIvLogo = createLogo();
        // 菜单容器
        mMenuContainer = FloatingSupport.buildMenuContainer(getContext());

        addMenuItem(mMenuContainer, mRightLayout);


        // 先添加菜单容器，再添加Logo，这样Logo在帧布局顶层
        windowContent.addView(mRightLayout ? mMenuContainer : mIvLogo);
        windowContent.addView(mRightLayout ? mIvLogo : mMenuContainer);

        windowContent.getBackground().setAlpha(0);
        windowContent.setVisibility(GONE);
        return windowContent;
    }

    /**
     * 将控件填充到菜单数据中
     *
     * @param context 上下文
     */
    private void stuffMenuItems(@NonNull Context context) {
        mMenuItemMap.put(ACCOUNT_ITEM, setMenuItemContent(context, "account", R.drawable.floating_account, 0));
        LinearLayout msgLayout = setMenuItemContent(context, "msg", R.drawable.floating_msg, 1, true);
        mMenuItemMap.put(MSG_ITEM, msgLayout);
        mMsgView = (MenuItemWithNumView) msgLayout.getChildAt(0);
        mMenuItemMap.put(COMMUNITY_ITEM, setMenuItemContent(context, "community", R.drawable.floating_community, 2));
        mMenuItemMap.put(CUSTOMER_ITEM, setMenuItemContent(context, "customer", R.drawable.floating_customer, 3));
        mMenuItemMap.put(ANNOUNCEMENT_ITEM, setMenuItemContent(context, "bulletin", R.drawable.floating_bulletin, 4));
        mMenuItemMap.put(HIDE_ITEM, setMenuItemContent(context, "hide", R.drawable.floating_hide, -1));
    }

    /**
     * 添加菜单item到菜单容器中
     *
     * @param linearLayout    菜单容器
     * @param layoutRightType 是否右边布局,true代表右手布局,倒序添加
     */
    private void addMenuItem(LinearLayout linearLayout, boolean layoutRightType) {
        if (layoutRightType) {
            for (int index = mMenuList.size() - 1; index >= 0; index--) {
                addViewByName(linearLayout, mMenuList.get(index));
            }
        } else {
            for (int index = 0; index < mMenuList.size(); index++) {
                addViewByName(linearLayout, mMenuList.get(index));
            }
        }
    }

    private void addViewByName(LinearLayout linearLayout, String itemName) {
        ViewGroup viewGroup = mMenuItemMap.get(itemName);
        if (null != viewGroup) {
            linearLayout.addView(viewGroup);
        }
    }

    /**
     * 创建悬浮按钮Logo，并设置拖放、点击展开或收起菜单的监听器
     *
     * @return 悬浮窗展示的图片
     */
    @NonNull
    private ImageView createLogo() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams((int) mLogoSize, (int) mLogoSize);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.floating_logo);
        // 设置拖放监听
        imageView.setOnTouchListener((view, event) -> handleOnTouch(event));
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
     * 创建悬浮窗菜单子项
     *
     * @param context       上下文
     * @param text          文案
     * @param drawableResId 子项图片资源
     * @param useMsgView    是否使用带消息提示的控件
     */
    private LinearLayout createMenuItem(Context context, String text, @DrawableRes int drawableResId, boolean useMsgView) {
        LinearLayout menuItem = buildMenuItemContainer(context);
        menuItem.addView(FloatingSupport.getMenuItemIcon(context, drawableResId, (int) mIconSize, useMsgView), 0);
        menuItem.addView(FloatingSupport.getMenuItemDesc(context, text, mTextSize), 1);
        return menuItem;
    }

    /**
     * 创建菜单项容器
     *
     * @param context 上下文
     */
    @NotNull
    private LinearLayout buildMenuItemContainer(Context context) {
        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuParams.gravity = Gravity.CENTER_VERTICAL;
        menuParams.setMargins(mRightLayout ? (int) mMargin : 0, 0, mRightLayout ? 0 : (int) mMargin, 0);

        LinearLayout menuItem = new LinearLayout(context);
        menuItem.setLayoutParams(menuParams);
        menuItem.setOrientation(LinearLayout.VERTICAL);
        return menuItem;
    }

    /**
     * 关闭悬浮按钮
     */
    public void hide() {
        mWindowContentView.setVisibility(GONE);
    }

    /**
     * 显示悬浮按钮
     */
    public void show() {
        checkVisible();
        checkSort();
        mWindowContentView.setVisibility(VISIBLE);
//        startCountdownTask();
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
        mMsgView.setNum(num);
    }

    /**
     * 对外提供方法用于初始化悬浮窗对象
     */
    public static FloatButton attach(Context context, FloatEventListener eventListener) {
        return FloatButtonProvider.create(context, eventListener);
    }

    public FloatButton setMsgVisible(boolean msgVisible) {

        mMenuVisible.put(MSG_ITEM, msgVisible);
        return this;
    }

    public FloatButton setCommunityVisible(boolean communityVisible) {
        mMenuVisible.put(COMMUNITY_ITEM, communityVisible);
        return this;
    }

    public FloatButton setCustomerVisible(boolean customerVisible) {
        mMenuVisible.put(CUSTOMER_ITEM, customerVisible);
        return this;
    }

    public FloatButton setAnnouncementVisible(boolean announcementVisible) {
        mMenuVisible.put(ANNOUNCEMENT_ITEM, announcementVisible);
        return this;
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
