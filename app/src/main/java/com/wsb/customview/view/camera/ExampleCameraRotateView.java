package com.wsb.customview.view.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.wsb.customview.R;

public class ExampleCameraRotateView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point1 = new Point(200, 100);
    Point point2 = new Point(600, 200);

    public ExampleCameraRotateView(Context context) {
        super(context);
    }

    public ExampleCameraRotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleCameraRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
    }
}
