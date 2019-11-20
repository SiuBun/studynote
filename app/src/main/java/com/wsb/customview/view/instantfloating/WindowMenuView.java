package com.wsb.customview.view.instantfloating;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.SparseArray;
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
    private SparseArray<FloatingMenuItems> mMenuItems;
    private ArrayList<Bitmap> mMenuBitmapList = new ArrayList<>();
    private ArrayList<RectF> mMenuRectFList = new ArrayList<>();
    /**
     * 默认左边布局
     */
    private MenuType mType = MenuType.LEFT;

    public WindowMenuView(Context context) {
        this(context, null);
    }

    public WindowMenuView(Context context, SparseArray<FloatingMenuItems> menuItems) {
        super(context);

        mPaint = preparePaint();

        parseMenuItems(menuItems);

    }


    private Paint preparePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
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
            mMenuBitmapList.add(DrawUtils.getBitmap(getResources(), DrawUtils.dp2px(16F), menuItems.get(index).getIcon()));

            float left = index * FwDrawUtil.ITEM_SIZE;
            float top = this.mFirstItemTop;
            mMenuRectFList.add(new RectF(left, top, left + FwDrawUtil.ITEM_SIZE, top + FwDrawUtil.ITEM_SIZE));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) (mMenuBitmapList.size() * FwDrawUtil.ITEM_SIZE), (int) FwDrawUtil.ITEM_SIZE);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (validData(mMenuItems)) {
            for (int index = 0; index < mMenuItems.size(); index++) {
                FloatingMenuItems menuItems = mMenuItems.get(index);

                mPaint.setColor(Color.parseColor("#7F000000"));

                RectF rect = mMenuRectFList.get(index);
                canvas.drawRect(rect, mPaint);

                drawIcon(canvas, index);
                drawTitle(canvas, index);
            }
        }
    }

    /**
     * 绘制标题
     */
    private void drawTitle(Canvas canvas, int index) {
        RectF rect = mMenuRectFList.get(index);
        FloatingMenuItems menuItems = mMenuItems.get(index);
        mPaint.setColor(getMenuTitleTextColor(menuItems));
        String titleText = menuItems.getTitleText();
        canvas.drawText(
                titleText,
                rect.centerX() - DrawUtils.getTextWidthByPaint(mPaint, titleText),
                rect.centerY() - DrawUtils.getTextHalfHeightByPaint(mPaint) +(FwDrawUtil.ICON_TITLE_SPACING),
                mPaint);
    }


    /**
     * 绘制图标
     */
    private void drawIcon(Canvas canvas, int index) {
        RectF rect = mMenuRectFList.get(index);
        Bitmap bitmap = mMenuBitmapList.get(index);
        float left = rect.centerX() - bitmap.getWidth() / 2F;
        float top = rect.centerY() - bitmap.getHeight() / 2F -(FwDrawUtil.ICON_TITLE_SPACING);
        canvas.drawBitmap(bitmap, left, top, mPaint);
    }

    private int getMenuTitleTextColor(FloatingMenuItems menuItems) {
        return menuItems.getTitleColor() == 0 ? Color.WHITE : menuItems.getTitleColor();
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

    /**
     * 菜单排列类型
     *
     * @author wsb
     */
    enum MenuType {
        /**
         * 菜单顺序从左到右
         */
        LEFT,
        /**
         * 菜单顺序从右到左
         */
        RIGHT,
        /**
         * 菜单顺序从上到下
         */
        TOP,
        /**
         * 菜单顺序从下到上
         */
        BOTTOM;
    }
}
