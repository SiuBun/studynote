package com.wsb.customview.fragment.practice

import android.graphics.Color
import android.view.View
import android.widget.Button
import com.wsb.customview.R
import com.wsb.customview.fragment.BaseFragment
import com.wsb.customview.view.practice.PolygonView
import com.wsb.customview.view.practice.RadarView

class RaDarFragment : BaseFragment() {
    private lateinit var radarView: RadarView
    override fun initView(v: View?) {
        val map = mutableMapOf("经济" to 0.8F, "伤害" to 0.6F, "走位" to 0.78F)
        map["助攻"] = 0.5F
        map["团战"] = 0.8F
        map["承受伤害"] = 0.2F
        v?.apply {

            val radarView = findViewById<RadarView>(R.id.radar).apply {
                lineColor = Color.WHITE
            }
            val btnColor = findViewById<Button>(R.id.btn_color).setOnClickListener {
                radarView.mainColor = Color.parseColor("#00574B")
            }
            val btnDimension = findViewById<Button>(R.id.btn_dimension).setOnClickListener {
                radarView.data = map
            }
            val btnData = findViewById<Button>(R.id.btn_data).setOnClickListener {
                map["承受伤害"] = 0.3F
                radarView.data = map
            }


        }

        v?.apply {
            val radarView = findViewById<PolygonView>(R.id.radar_chart)

//            radarView.setMaxValue(100F)
//            radarView.setCircleCount(5)
            radarView.setPolygonData(
                hashMapOf(
                    "经济" to 80F,
                    "伤害" to 60F,
                    "走位" to 78F,
                    "助攻" to 50F,
                    "团战" to 80F,
                    "承受伤害" to 20F,
                    "中立获取" to 40F,
                    "死亡" to 40F
                ), 4, 100F
            )
            radarView.setLayerColors(
                listOf(
                    Color.parseColor("#FFC19595"),
                    Color.parseColor("#FFECAAAA")
                )
            )
        }
    }

    override fun getFragmentLayout(): Int {
        return R.layout.radar
    }
}