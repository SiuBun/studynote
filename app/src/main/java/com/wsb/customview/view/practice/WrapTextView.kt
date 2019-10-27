package com.wsb.customview.view.practice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.DrawUtils

class WrapTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = DrawUtils.dp2px(20F)
    }

    private var normalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = DrawUtils.dp2px(20F)
    }

    private var fontMetrics: Paint.FontMetrics = normalPaint.fontMetrics

    private val imageWidth = DrawUtils.dp2px(150F)
    private val imageOffset = DrawUtils.dp2px(70F)

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
            drawText(textContent, 0, breakText, 0F, normalPaint.fontSpacing, normalPaint)

            val fontSpacing = normalPaint.fontSpacing
            var verticalOffset = fontSpacing
            var start = 0

            while (start < textContent.length) {
                val textTop = verticalOffset + fontMetrics.top
                val textBottom = verticalOffset + fontMetrics.bottom

                val realWidth = if ((textBottom > imageOffset && textBottom < imageOffset + bitmap.height) || (textTop > imageOffset && textTop < imageOffset + bitmap.height))
                    width - imageWidth
                else
                    width.toFloat()

                val count = normalPaint.breakText(textContent, start, textContent.length, true, realWidth, cutArray)
                drawText(textContent, start, start + count, 0F, verticalOffset, normalPaint)
                verticalOffset += fontSpacing
                start += count

            }

        }
    }
}