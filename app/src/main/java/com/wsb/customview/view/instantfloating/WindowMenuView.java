package com.wsb.customview.view.instantfloating;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.wsb.customview.utils.DrawUtils;

import java.util.ArrayList;

/**
 * 悬浮窗菜单控件
 * <p>
 * 包含各个菜单项
 *
 * @author wsb
 */
class WindowMenuView extends View {

    private Paint mPaint;

    /**
     * 菜单控件的数据源
     * */
    private SparseArray<FloatingMenuItems> mMenuItems;

    /**
     * 菜单所需要的icon图片对象
     * */
    private ArrayList<Bitmap> mMenuBitmapList = new ArrayList<>();

    /**
     * 菜单绘制的空间
     * */
    private ArrayList<RectF> mMenuRectFList = new ArrayList<>();
    /**
     * 默认左边布局
     */
    private MenuType mType = MenuType.LEFT;
    private ValueAnimator mAnimator;

    /**
     * 菜单点击监听对象
     */
    private OnMenuClickListener mOnMenuClickListener;

    public WindowMenuView(Context context) {
        this(context, null);
    }

    public WindowMenuView(Context context, SparseArray<FloatingMenuItems> menuItems) {
        super(context);
        mPaint = preparePaint();
        mAnimator = ObjectAnimator
                .ofPropertyValuesHolder(
                        WindowMenuView.this,
                        PropertyValuesHolder.ofFloat("alpha", 0F, 1F)
//                        ,PropertyValuesHolder.ofFloat("scaleX", 0F, 1F)
                )
                .setDuration(FwDrawUtil.ANIMATOR_DURATION);
        parseMenuItems(menuItems);

    }


    private Paint preparePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(DrawUtils.dp2px(1F));
        paint.setTextSize(FwDrawUtil.TEXT_SIZE);
        paint.setTextSize(FwDrawUtil.TEXT_SIZE);
        paint.setTextAlign(Paint.Align.LEFT);
        return paint;
    }

    private void parseMenuItems(SparseArray<FloatingMenuItems> menuItems) {
        mMenuItems = menuItems;
        mMenuBitmapList.clear();
        for (int index = 0; index < menuItems.size(); index++) {
            mMenuBitmapList.add(DrawUtils.getBitmap(getResources(), FwDrawUtil.ICON_SIZE, menuItems.get(index).getIcon()));

            float left = index * FwDrawUtil.ITEM_SIZE;
            float top = this.mFirstItemTop;
            mMenuRectFList.add(new RectF(left, top, left + FwDrawUtil.ITEM_SIZE, top + FwDrawUtil.ITEM_SIZE));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) (mMenuItems.size() * FwDrawUtil.ITEM_SIZE), (int) FwDrawUtil.ITEM_SIZE);
    }

    /**
     * 菜单项左边的默认偏移值，这里是0
     */
    private int mItemLeft = 0;

    /**
     * 菜单项的最小y值，上面起始那条线
     */
    private float mFirstItemTop;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int itemsIndex = mType.initItemStartIndex(mMenuItems.size());
                for (int i = 0; i < mMenuRectFList.size(); i++) {
                    if (mOnMenuClickListener != null && isPointInRect(new PointF(event.getX(), event.getY()), mMenuRectFList.get(i))) {
                        mOnMenuClickListener.onItemClick(itemsIndex, mMenuItems.get(itemsIndex).getTitleText());
                        return true;
                    }
                    itemsIndex = mType.updateItemIndex(itemsIndex);
                }
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isPointInRect(PointF point, RectF targetRect) {
        return point.x >= targetRect.left && point.x <= targetRect.right && point.y >= targetRect.top && point.y <= targetRect.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (validData(mMenuItems)) {
            int itemsIndex = mType.initItemStartIndex(mMenuItems.size());

            for (int index = 0; index < mMenuRectFList.size(); index++) {
//                调试辅助
//                mPaint.setColor(getResources().getColor(R.color.floating_window_bg));
//                canvas.drawRect(mMenuRectFList.get(index), mPaint);

                drawIcon(canvas, index, itemsIndex);
                drawTitle(canvas, index, itemsIndex);
                itemsIndex = mType.updateItemIndex(itemsIndex);
            }
        }
    }

    /**
     * 绘制标题
     */
    private void drawTitle(Canvas canvas, int rectfIndex, int itemsIndex) {
        RectF rect = mMenuRectFList.get(rectfIndex);
        FloatingMenuItems menuItems = mMenuItems.get(itemsIndex);
        mPaint.setColor(getMenuTitleTextColor(menuItems));
        String titleText = menuItems.getTitleText();
        canvas.drawText(
                titleText,
                rect.centerX() - DrawUtils.getTextWidthByPaint(mPaint, titleText),
                rect.centerY() - DrawUtils.getTextHalfHeightByPaint(mPaint) + (FwDrawUtil.ICON_TITLE_SPACING),
                mPaint);
    }


    /**
     * 绘制图标
     */
    private void drawIcon(Canvas canvas, int rectfIndex, int itemsIndex) {
        RectF rect = mMenuRectFList.get(rectfIndex);
        Bitmap bitmap = mMenuBitmapList.get(itemsIndex);
        float left = rect.centerX() - bitmap.getWidth() / 2F;
        float top = rect.centerY() - bitmap.getHeight() / 2F - (FwDrawUtil.ICON_TITLE_SPACING/2);
        canvas.drawBitmap(bitmap, left, top, mPaint);
    }

    private int getMenuTitleTextColor(FloatingMenuItems menuItems) {
        return menuItems.getTitleColor() == 0 ? Color.WHITE : menuItems.getTitleColor();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setType(@NonNull MenuType menuType) {
        if (mType != menuType) {
            this.mType = menuType;
            invalidate();
        }
    }

    public void setMenuItems(SparseArray<FloatingMenuItems> menuItems) {
        if (validData(menuItems)) {
            parseMenuItems(menuItems);
            invalidate();
        }
    }

    private boolean validData(SparseArray<FloatingMenuItems> menuItems) {
        return null != menuItems && menuItems.size() > 0;
    }

    void showOrHide(boolean showed) {
        if (showed) {
            mAnimator.reverse();
        } else {
            mAnimator.start();
        }
    }

    /**
     * 菜单排列类型
     *
     * @author wsb
     */
    enum MenuType implements MenuTypeBehavior {
        /**
         * 菜单顺序从左到右
         */
        LEFT {
            @Override
            public int initItemStartIndex(int size) {
                return 0;
            }

            @Override
            public int updateItemIndex(int index) {
                return index += 1;
            }
        },
        /**
         * 菜单顺序从右到左
         */
        RIGHT {
            @Override
            public int initItemStartIndex(int size) {
                return size - 1;
            }

            @Override
            public int updateItemIndex(int index) {
                return index -= 1;
            }
        },
        /**
         * 菜单顺序从上到下
         */
        TOP {
            @Override
            public int initItemStartIndex(int size) {
                return 0;
            }

            @Override
            public int updateItemIndex(int index) {
                return index += 1;
            }
        },
        /**
         * 菜单顺序从下到上
         */
        BOTTOM {
            @Override
            public int initItemStartIndex(int size) {
                return size - 1;
            }

            @Override
            public int updateItemIndex(int index) {
                return index -= 1;
            }
        };


    }

    /**
     * 排列类型行为
     *
     * @author wsb
     */
    interface MenuTypeBehavior {
        /**
         * 得到该排列类型下的初始绘制下标
         *
         * @param size 数据源大小
         * @return 从数据源钟取值的下标
         */
        int initItemStartIndex(int size);

        /**
         * 更新该排列类型下的绘制下标
         *
         * @param index 当前下标
         * @return 下一个取值下标
         */
        int updateItemIndex(int index);
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.mOnMenuClickListener = onMenuClickListener;
    }

    /**
     * 菜单点击监听回调
     *
     * @author wsb
     * */
    public interface OnMenuClickListener {
        /**
         * 当菜单项被点击时候调用
         *
         * @param position 被点击的菜单项在数据里的下标
         * @param title 被点击的菜单项在数据里的标题
         * */
        void onItemClick(int position, String title);

    }
}
