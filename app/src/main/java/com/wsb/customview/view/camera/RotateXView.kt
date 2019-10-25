package com.wsb.customview.view.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class RotateXView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        var point1 = Point(200, 200)
        var point2 = Point(600, 200)
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val center1X = point1.x + bitmapWidth / 2
        val center1Y = point1.y + bitmapHeight / 2
        val center2X = point2.x + bitmapWidth / 2
        val center2Y = point2.y + bitmapHeight / 2

        canvas?.also {

            it.save()
            Camera().apply {
                save()
                //旋转 Camera 的三维空间
                rotateX(30f)
                // 把旋转投影到 Canvas
                applyToCanvas(it)
                restore()
            }

            it.drawBitmap(bitmap, point1.x.toFloat(), point1.y.toFloat(), paint)
            it.restore()


            it.save()
            Camera().apply {
                save()
                //旋转 Camera 的三维空间
                rotateY(30f)
                // 把旋转投影到 Canvas
                applyToCanvas(it)
                restore()
            }

            it.drawBitmap(bitmap, point2.x.toFloat(), point2.y.toFloat(), paint)
            it.restore()
        }
    }
}