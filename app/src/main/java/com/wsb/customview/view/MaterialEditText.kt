package com.wsb.customview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.animation.AnticipateInterpolator
import com.wsb.customview.DrawUtils
import java.util.concurrent.TimeUnit

class MaterialEditText(context: Context?, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private val textSizeValue = DrawUtils.dip2px(14F)
    private val textMarginValue = DrawUtils.dip2px(8F)

    private val offsetLeftValue = DrawUtils.dip2px(4F)
    private val offsetVerticalValue = DrawUtils.dip2px(38F)
    private val offsetExtraValue = DrawUtils.dip2px(16F)


    var showFraction: Float = 0F
        set(value) {
            if (value >= 0) {
                field = value
                invalidate()
            }
        }

    val uncomplete = Undone()
    val complete = Completed()
    private var fractionState: FractionState = uncomplete

    val showFractionAnimator: ObjectAnimator = ObjectAnimator.ofFloat(this@MaterialEditText, "showFraction", 0F, 1F).apply {
        duration = TimeUnit.MILLISECONDS.toMillis(300)
        interpolator = AnticipateInterpolator()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        textAlign = Paint.Align.LEFT
        textSize = textSizeValue
    }

    init {

        setPadding(
                paddingLeft, (paddingTop + textSizeValue + textMarginValue).toInt(), paddingRight, paddingBottom
        )

        val callback = object : RespCallback {
            override fun changeState(state: Boolean) {
                fractionState = if (state) {
                    showFractionAnimator.start()
                    complete
                } else {
                    showFractionAnimator.reverse()
                    uncomplete
                }
            }
        }

        val textChangeListener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                fractionState.responseText(s, callback)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }
        addTextChangedListener(textChangeListener)
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.run {
            drawColor(Color.parseColor("#e7e7e7"))
            super.onDraw(canvas)
            drawText(
                    hint.toString(),
                    offsetLeftValue,
                    offsetVerticalValue - offsetExtraValue * showFraction,
                    paint.apply {
                        alpha = (showFraction * 0xff).toInt()
                    })
        }

    }

}

abstract class FractionState {
    abstract fun responseText(s: CharSequence?, callback: RespCallback)
}

/**
 * 已完成状态下发现文字内容为空会做响应,并更改状态为未完成
 *
 * @author wsb
 * */
class Completed : FractionState() {
    override fun responseText(s: CharSequence?, callback: RespCallback) {
        if (TextUtils.isEmpty(s)) {
            callback.changeState(false)
        }
    }
}

/**
 * 未完成状态下发现文字内容为不空会做响应,并更改状态为已完成
 *
 * @author wsb
 * */
class Undone : FractionState() {
    override fun responseText(s: CharSequence?, callback: RespCallback) {
        if (!TextUtils.isEmpty(s)) {
            callback.changeState(true)
        }
    }
}

/**
 * 完成回调
 *
 * @author wsb
 * */
interface RespCallback {
    fun changeState(state: Boolean)
}
