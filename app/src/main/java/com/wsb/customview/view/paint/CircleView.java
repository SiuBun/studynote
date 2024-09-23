package com.wsb.customview.view.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * TODO: document your custom view class.
 */
public class CircleView extends View {

    private Paint paint = new Paint();

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 填充模式
        paint.setStyle(Paint.Style.FILL);
        // 绘制扇形
        canvas.drawArc(200, 100, 400, 500, -110, 100, true, paint);

        // 绘制弧形
//        canvas.drawArc(200, 100, 400, 500, 20, 140, false, paint);
        // 画线模式
//        paint.setStyle(Paint.Style.STROKE);
        // 绘制不封口的弧形
//        canvas.drawArc(200, 100, 400, 500, 180, 60, false, paint);

    }
}
