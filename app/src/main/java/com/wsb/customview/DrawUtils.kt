package com.wsb.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue

object DrawUtils {
    @JvmStatic
    @Deprecated("新版本上因为上下文移除显得这个方法比较多余", ReplaceWith("fun dp2px(dipValue: Float): Float"),DeprecationLevel.WARNING)
    fun dp2px(context: Context, dipValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    @JvmStatic
    fun sp2px(context: Context, spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    @JvmStatic
    fun dp2px(dipValue: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, Resources.getSystem().displayMetrics)

    @JvmStatic
    fun getAvatar(resources: Resources,width: Float): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.batman, options)
        options.apply {
            inJustDecodeBounds = false
            inDensity = outWidth
            inTargetDensity = width.toInt()
        }
        return BitmapFactory.decodeResource(resources, R.drawable.batman, options)
    }

    fun getZForCamara(): Float = -4*Resources.getSystem().displayMetrics.density


}
