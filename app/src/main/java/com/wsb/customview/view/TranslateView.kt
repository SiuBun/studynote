package com.wsb.customview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.R

class TranslateView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr){

//    init {
//        setLayerType(LAYER_TYPE_SOFTWARE,null)
//    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maps)
        val left = ((width - bitmap.width) / 2).toFloat()
        val top = ((height - bitmap.height) / 2).toFloat()

        canvas?.run {
            val logo = BitmapFactory.decodeResource(context.resources, R.drawable.batman_logo)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//                    .apply {
//                shader = ComposeShader(BitmapShader(logo,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP),BitmapShader(batman,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP),PorterDuff.Mode.DST_OVER)
//            }

//            加上 Canvas.save() 和 Canvas.restore() 来及时恢复绘制范围
            save()

            translate(0F,400F)

            drawBitmap(bitmap,left,top,paint)
//            drawCircle(600F,300F,400F,paint)


            restore()
            drawBitmap(logo,0F,0F,paint)
        }
    }
}