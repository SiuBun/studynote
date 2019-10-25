package com.wsb.customview.view.paint

import android.content.Context
import android.graphics.*
import android.view.View
import com.wsb.customview.R

class XfermodeView(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {


            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#affdb2")
            }

            val saveLayer = saveLayer(null, null, Canvas.ALL_SAVE_FLAG)

            drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.batman),0F,0F,paint)

//                处理源图像和  View 已有内容的关系
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

            drawBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.batman_logo),0F,0F,paint)


            paint.xfermode = null

            restoreToCount(saveLayer)
        }
    }
}
