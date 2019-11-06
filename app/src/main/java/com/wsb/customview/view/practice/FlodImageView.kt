package com.wsb.customview.view.practice

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wsb.customview.utils.DrawUtils

class FlodImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var IMAGE_WIDTH = DrawUtils.dp2px(200F)
    private var IMAGE_OFFSET = DrawUtils.dp2px(50F)
    private var CLIP_OFFSET = DrawUtils.dp2px(50F)
    private val bitmap = DrawUtils.getAvatar(resources = resources, width = IMAGE_WIDTH)
    private var centerX = IMAGE_OFFSET+bitmap.width/2
    private var centerY = IMAGE_OFFSET+bitmap.height/2
    var canvasDegress = 0F
    set(value) {
        field = value
        invalidate()
    }

    var cameraXDegress = 0F
    set(value) {
        field = value
        invalidate()
    }

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var canmera = Camera()

    init {
        canmera.setLocation(0F,0F, DrawUtils.getZForCamara())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawColor(Color.parseColor("#e7e7e7"))
            save()
            canmera.apply {
//            canvas搭配camera移动时候考虑逆向移动。常理说应该是往让图像左上偏移再右下移回来，但是代码里思路应该反过来，让画布先往右下，再往左上来实现
                this@run.translate(centerX,centerY)
                this@run.rotate(-canvasDegress)
//                clip同理,想法是先裁画布再翻camera，所以要先翻后裁
                clipRect((-IMAGE_WIDTH/2) -CLIP_OFFSET, (-IMAGE_WIDTH/2) -CLIP_OFFSET, (IMAGE_WIDTH/2) +CLIP_OFFSET, 0F)
                this@run.rotate(canvasDegress)
                this@run.translate(-centerX,-centerY)
            }
            drawBitmap(bitmap,IMAGE_OFFSET,IMAGE_OFFSET,paint)
            restore()

            save()
            canmera.apply {
//            canvas搭配camera移动时候考虑逆向移动。常理说应该是往左上偏移再右下移回来，但是代码里思路应该反过来.
                this@run.translate(centerX,centerY)
                this@run.rotate(-canvasDegress)
                save()
                rotateX(cameraXDegress)
                applyToCanvas(this@run)
                restore()
//                clip同理,想法是先裁再翻，所以要先翻后裁
                clipRect((-IMAGE_WIDTH/2) -CLIP_OFFSET,0F, (IMAGE_WIDTH/2) +CLIP_OFFSET, (IMAGE_WIDTH/2) +CLIP_OFFSET)
                this@run.rotate(canvasDegress)
                this@run.translate(-centerX,-centerY)
            }
            drawBitmap(bitmap,IMAGE_OFFSET,IMAGE_OFFSET,paint)
            restore()
        }

    }

}