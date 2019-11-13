package com.wsb.customview.view.floating;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 悬浮窗菜单带文字提示控件
 *
 * @author wsb
 */

public class MenuItemWithNumView extends AppCompatImageView {

    /**
     * 要显示的数量
     */
    private int mNum = 0;
    /**
     * 红色圆圈的半径
     */
    private float mRadius;
    /**
     * 圆圈内数字的半径
     */
    private float mTextSize;
    /**
     * 右边和上边内边距
     */
    private int mPaddingRight;
    private int mPaddingTop;

    /**
     * 数字画笔
     * */
    private Paint paint = new Paint();

    public MenuItemWithNumView(Context context) {
        super(context);
    }

    public MenuItemWithNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuItemWithNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        invalidate();
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
        invalidate();
    }

    /**
     * 设置显示的数量
     *
     * @param num 数量
     */
    public void setNum(int num) {
        this.mNum = num;
        //重新绘制画布
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //初始化边距
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNum > 0) {
            //初始化半径
            mRadius = getWidth() / 4;
            //初始化字体大小
            mTextSize = mNum < 10 ? mRadius + 5 : mRadius;



            //设置抗锯齿
            paint.setAntiAlias(true);
            //设置颜色为红色
            paint.setColor(0xffff4444);
            //设置填充样式为充满
            paint.setStyle(Paint.Style.FILL);
            //画圆
            canvas.drawCircle(getWidth() - mRadius - (float)mPaddingRight / 2, mRadius + (float)mPaddingTop / 2, mRadius, paint);
            //设置颜色为白色
            paint.setColor(0xffffffff);
            //设置字体大小
            paint.setTextSize(mTextSize);
            //画数字
            canvas.drawText(Integer.toString(mNum < 99 ? mNum : 99),
                    mNum < 10 ? getWidth() - mRadius - mTextSize / 4 - (float)mPaddingRight / 2
                            : getWidth() - mRadius - mTextSize / 2 - (float)mPaddingRight / 2,
                    mRadius + mTextSize / 3 + (float)mPaddingTop / 2, paint);
        }
    }


}
