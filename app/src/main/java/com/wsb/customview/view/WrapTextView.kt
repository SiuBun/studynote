package com.wsb.customview.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.DrawUtils

class WrapTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = DrawUtils.dip2px(20F)
    }

    private var normalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        textSize = DrawUtils.dip2px(20F)
    }

    private val imageWidth = DrawUtils.dip2px(150F)
    private val imageOffset = DrawUtils.dip2px(70F)

    private var bitmap: Bitmap = DrawUtils.getAvatar(resources, imageWidth)

    private var cutArray = floatArrayOf(1F)

    var textContent: String = "fadsgvfzvf iuh i n  i m nmizsjo jsndsju  uiwen944cxm  yhasbndjask dfsaisd woxj owj zpzlaj sif sfhidoa asdf dsfsa as fidhfoa iashdfud ao dfhas aoekn wi s aso fas  sjff d aad fj fd laksfda dfaldfkd fjndsah ja ase sjk aksdf e ks fei ws wi fadsgvfzvf iuh i n  i m nmizsjo jsndsju  uiwen944cxm  yhasbndjask dfsaisd woxj owj zpzlaj sif sfhidoa asdf dsfsa as fidhfoa iashdfud ao dfhas aoekn wi s aso fas  sjff d aad fj fd laksfda dfaldfkd fjndsah ja ase sjk aksdf e ks fei ws wi"
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.run {
            drawColor(Color.parseColor("#e7e7e7"))

//            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                StaticLayout.Builder.obtain(textContent, 0, textContent.length, textPaint, width).build()
//            } else {
//                StaticLayout(textContent, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1F, 0F, false)
//            }
//
//            builder.draw(canvas)

            drawBitmap(bitmap, width - imageWidth, imageOffset, normalPaint)


            val breakText = normalPaint.breakText(textContent, true, width.toFloat(), cutArray)
            drawText(textContent,0,breakText,0F, normalPaint.fontSpacing,normalPaint)



        }
    }
}