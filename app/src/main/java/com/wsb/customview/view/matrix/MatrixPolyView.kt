package com.wsb.customview.view.matrix

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class MatrixPolyView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val startLeft = ((width - bitmap.width) / 2).toFloat()
        val startTop = ((height - bitmap.height) / 2).toFloat()
        canvas?.run {
            val paint = Paint()
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)

            val matrix = Matrix().apply {
                reset()
                val right = startLeft + bitmap.width
                val bottom = startTop + bitmap.height
                val startArray = floatArrayOf(startLeft, startTop, right, startTop, startLeft, bottom, right, bottom)
                val stopArray = floatArrayOf(startLeft - 10, startTop + 50, right + 120, startTop - 90, startLeft + 20, bottom + 30, right + 20, bottom + 60)
                setPolyToPoly(startArray,0,stopArray,0,4)
            }


            save()

//            使用 Canvas.setMatrix(matrix) 或 Canvas.concat(matrix) 来把几何变换应用到 Canvas,实现Canvas位移缩放旋转错切一样的效果
            concat(matrix)
            drawBitmap(bitmap,startLeft,startTop,paint)

//            drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.batman),matrix,paint)

            restore()
        }
    }
}