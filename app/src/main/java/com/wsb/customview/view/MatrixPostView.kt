package com.wsb.customview.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class MatrixPostView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val left = ((width - bitmap.width) / 2).toFloat()
        val top = ((height - bitmap.height) / 2).toFloat()

        canvas?.run {
            val paint = Paint()

            val matrix = Matrix().apply {
                reset()
                postTranslate(300F,200F)
//                postRotate()
//                postScale(0.3F,0.8F)
//                postSkew()
            }


            save()

//            使用 Canvas.setMatrix(matrix) 或 Canvas.concat(matrix) 来把几何变换应用到 Canvas,实现Canvas位移缩放旋转错切一样的效果
            concat(matrix)
            drawBitmap(bitmap,left,top,paint)

//            drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.batman),matrix,paint)

            restore()
        }
    }
}