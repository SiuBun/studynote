package com.wsb.customview.view.paint

import android.content.Context
import android.graphics.*
import android.view.View
import com.wsb.customview.R

class ShadowMaskView(context: Context?) : View(context) {
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.run {
            val paint = Paint().apply {
                textSize = 100F
                color = Color.parseColor("#d5b7ae")
            }
            paint.setShadowLayer(10F, 0F, 0F, Color.RED)
            drawText("Hello Custom", 80F, 300F, paint)
            paint.clearShadowLayer()

            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.what_the_fuck)
            paint.maskFilter = BlurMaskFilter(100F, BlurMaskFilter.Blur.INNER)
            drawBitmap(bitmap, 100F, 400F, paint)
        }
    }
}
