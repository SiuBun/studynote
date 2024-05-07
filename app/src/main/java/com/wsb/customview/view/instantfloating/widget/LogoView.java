package com.wsb.customview.view.instantfloating.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.wsb.customview.utils.DrawUtils;
import com.wsb.customview.view.instantfloating.utils.FwDrawUtil;

/**
 * 悬浮窗logo控件
 * <p>
 * 负责半隐藏和复位,并接收触摸移动
 *
 * @author wsb
 */
public class LogoView extends View {
    private Paint mPaint;
    private Bitmap mLogoBitmap;
    private State mState;

    public LogoView(Context context) {
        this(context, null);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initConfig();
    }

    private void initConfig() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mState = State.NORMAL;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mState == State.NORMAL ? widthMeasureSpec : widthMeasureSpec / 2, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        switch (mState) {
            case LEFT_SHADING:
                canvas.translate(-FwDrawUtil.LOGO_SIZE / 2, 0);
                mPaint.setAlpha((int) (255*0.5));
                break;
            case RIGHT_SHADING:
                canvas.translate(FwDrawUtil.LOGO_SIZE / 2, 0);
                mPaint.setAlpha((int) (255*0.5));
                break;
            default:
                mPaint.setAlpha(255);
                break;
        }

        if (null != mLogoBitmap) {
            canvas.drawBitmap(mLogoBitmap, 0, 0, mPaint);
        }
        canvas.restore();
    }

    public void setDrawable(@DrawableRes int logoDrawable) {
        mLogoBitmap = DrawUtils.getBitmap(getResources(), FwDrawUtil.LOGO_SIZE, logoDrawable);
        invalidate();
    }

    public void setState(State state) {
        mState = state;
        invalidate();
    }

    /**
     * 当前logo展示状态
     */
    public enum State {
        /**
         * 正常展示
         */
        NORMAL,
        /**
         * 隐藏左边
         */
        LEFT_SHADING,
        /**
         * 隐藏右边
         */
        RIGHT_SHADING
    }
}
