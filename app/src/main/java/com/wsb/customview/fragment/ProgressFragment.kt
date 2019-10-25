package com.wsb.customview.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.wsb.customview.R
import com.wsb.customview.view.AcceleratorView
import com.wsb.customview.view.ProgressView
import java.util.concurrent.TimeUnit

class ProgressFragment : BaseFragment() {
    override fun getFragmentLayout(): Int {
        return R.layout.progress
    }

    private lateinit var prov: ProgressView
    private lateinit var acv: AcceleratorView
    private lateinit var btnExecute: Button
    private lateinit var etValue: EditText

    override fun initView(v: View?) {
        v?.run {
            etValue = findViewById(R.id.et_value)

            prov = findViewById<ProgressView>(R.id.prov).also {
                it.animate().alpha(0F).scaleX(0F).scaleY(0F)
            }


            btnExecute = findViewById<Button>(R.id.btn_execute).apply {
                setOnClickListener {
                    val value = etValue.text.toString().takeIf { it.isNotEmpty() }?.toInt()
                    value?.run {
                        prov.defineValue(value)
                        acv.defineValue(value)
                    }
                }
            }

            acv = findViewById<AcceleratorView>(R.id.acv).apply {
//                animate().alpha(0F).scaleX(0F).scaleY(0F)
            }

            postDelayed(
                    {
                        prov.animate().alpha(1F).scaleX(1F).scaleY(1F)

                        ObjectAnimator.ofInt(prov, "value", 0, 100).run {
                            interpolator = LinearOutSlowInInterpolator()
                            duration = TimeUnit.SECONDS.toMillis(1)
                            start()
                        }

                        AnimatorSet().run {
                            playTogether(
                                    ObjectAnimator.ofInt(acv,"value",0,70),
                                    ObjectAnimator.ofFloat(acv,"alpha",0F,1F),
                                    ObjectAnimator.ofFloat(acv,"scaleX",0F,1F),
                                    ObjectAnimator.ofFloat(acv,"scaleY",0F,1F)
                            )
                            interpolator = FastOutSlowInInterpolator()
                            duration = TimeUnit.SECONDS.toMillis(1)
                            start()
                        }


                    },
                    TimeUnit.SECONDS.toMillis(1)
            )

        }
    }

}