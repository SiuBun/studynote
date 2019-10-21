package com.wsb.customview.view

import android.content.Context
import android.graphics.*
import android.view.View
import com.wsb.customview.R

class ComposeView(context: Context?) : View(context) {
    init {
        //硬件加速下是不支持两个相同类型的 Shader 的，所以这里也需要关闭硬件加速才能看到效果
        setLayerType(LAYER_TYPE_SOFTWARE,null)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            drawColor(Color.parseColor("#655dcf"))

            val batman = BitmapFactory.decodeResource(context.resources, R.drawable.batman)
            val batmanLogo = BitmapFactory.decodeResource(context.resources, R.drawable.batman_logo)

            val batmanShader = BitmapShader(batman, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val batmanLogoShader = BitmapShader(batmanLogo, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            paint.shader = ComposeShader(batmanShader,batmanLogoShader,PorterDuff.Mode.SRC_OVER)
//            drawCircle(270F,270F,200F,paint)


//            drawXXX() 方法会对每个像素都进行过滤后再绘制出来 下为原样
            paint.colorFilter = LightingColorFilter(0xffffff,0x000000)

//            去绿
            paint.colorFilter = LightingColorFilter(0xff00ff,0x000000)

//            加蓝
            paint.colorFilter = LightingColorFilter(0xffffff,0x000030)

            drawCircle(270F,270F,200F,paint)



        }
    }

}
