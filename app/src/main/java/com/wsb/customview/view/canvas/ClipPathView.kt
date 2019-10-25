package com.wsb.customview.view.canvas

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class ClipPathView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val left = ((width - bitmap.width) / 2).toFloat()
        val top = ((height - bitmap.height) / 2).toFloat()
        canvas?.run {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            save()

            clipPath(Path().apply {
                addCircle(left+300F,top+300F,250F,Path.Direction.CW)
                fillType = Path.FillType.INVERSE_WINDING
            })
            drawBitmap(bitmap, left, top, paint)

            restore()
        }
    }

}