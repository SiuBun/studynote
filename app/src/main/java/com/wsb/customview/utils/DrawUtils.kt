package com.wsb.customview.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import androidx.annotation.DrawableRes
import com.wsb.customview.R
import kotlin.math.abs

object DrawUtils {
    @JvmStatic
    @Deprecated("新版本上因为上下文移除显得这个方法比较多余", ReplaceWith("fun dp2px(dipValue: Float): Float"), DeprecationLevel.WARNING)
    fun dp2px(context: Context, dipValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    @JvmStatic
    @Deprecated("新版本上因为上下文移除显得这个方法比较多余", ReplaceWith("fun dp2px(dipValue: Float): Float"), DeprecationLevel.WARNING)
    fun sp2px(context: Context, spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    /**
     * dp值转实际px值
     *
     * @param dipValue 预期的dp值
     * @return 最终在屏幕上的px值
     * */
    @JvmStatic
    fun dp2px(dipValue: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, Resources.getSystem().displayMetrics)

    @JvmStatic
    fun sp2px(spValue: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, Resources.getSystem().displayMetrics)

    @JvmStatic
    @JvmOverloads
    fun getBitmap(resources: Resources, width: Float, @DrawableRes id: Int = R.drawable.batman): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, id, options)
        options.apply {
            inJustDecodeBounds = false
            inDensity = outWidth
            inTargetDensity = width.toInt()
        }
        return BitmapFactory.decodeResource(resources, id, options)
    }

    /**
     * 根据当前设备屏幕密度获得合理的相机对象距离倍数
     *
     * @return 倍数值
     * */
    fun getZForCamara(): Float = -4 * Resources.getSystem().displayMetrics.density

    /**
     * 是否为窄图,即宽是否小于高
     *
     * @param bitmap 测量的图片对象
     * @return true代表图片为窄图,false为扁图
     * */
    fun narrowBitmap(bitmap: Bitmap): Boolean = bitmap.width < bitmap.height


    /**
     * 根据画笔参数和文字内容获取文字所占长度
     *
     * @param paint 画笔
     * @param text 文字内容
     * */
    @JvmStatic
    fun getTextWidthByPaint(paint: Paint, text: String): Float = paint.measureText(text) / 2

    /**
     * 根据画笔参数获取行高
     *
     * 适用于文字内容变化的情况
     * @param paint 画笔
     * */
    @JvmStatic
    fun getTextHalfHeightByPaint(paint: Paint): Float = paint.fontMetrics.let {
        (it.descent + it.ascent)/2F
    }

    /**
     * 根据画笔参数获取行高
     *
     * 适用于文字内容固定不变情况
     * @param paint 画笔
     * */
    @JvmStatic
    fun getFixedTextHeightByPaint(paint: Paint, text: String): Float {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.let {
            abs(it.bottom.toFloat() - it.top.toFloat())
        }
    }

}
