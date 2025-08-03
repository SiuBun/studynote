package com.wsb.customview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.wsb.customview.databinding.ActivityMainBinding
import com.wsb.customview.fragment.CameraFragment
import com.wsb.customview.fragment.CanvasFragment
import com.wsb.customview.fragment.MatrixFragment
import com.wsb.customview.fragment.MultiTouchFragment
import com.wsb.customview.fragment.paint.PaintFragment
import com.wsb.customview.fragment.practice.PracticeFragment
import com.wsb.customview.fragment.practice.ScalaImageFragment
import com.wsb.customview.motionlayout.MotionLayoutActivity
import com.wsb.customview.motionlayout.MotionSearchActivity
import com.wsb.customview.shareelement.ShareElementActivity
import com.wsb.customview.utils.LogUtils
import com.wsb.customview.view.WaveView
import com.wsb.customview.view.instantfloating.data.FloatingMenuItems
import com.wsb.customview.view.instantfloating.strategy.LayoutType
import com.wsb.customview.view.instantfloating.widget.InstantFloatingWindow

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var mWindow: InstantFloatingWindow? = null
    private lateinit var mBinding: ActivityMainBinding

    private var sparseArray: SparseArray<FloatingMenuItems> = SparseArray()
    private var showing = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llt_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initListener()
        
        val menuNameList = ArrayList<String>()
        val icons = intArrayOf(R.drawable.floating_account, R.drawable.floating_msg, R.drawable.floating_hide)
        menuNameList.add("account")
        menuNameList.add("msg")
        menuNameList.add("hide")
        for (i in menuNameList.indices) {
            sparseArray.append(i, FloatingMenuItems(menuNameList[i], icons[i]))
        }

        mBinding.waveView.apply {
            setWaveHeight(100f)
            setWaveFrequency(0.1f)
            setWaveSpeed(6f)
            setWavePhase(0f)
            setSecondWavePhaseOffset(90f)
            startWaveAnimation()
        }

    }

    private fun initListener() {
        mBinding.apply {
            btnPaint.setOnClickListener {
                Log.d(TAG, "paintPage")
                getCommit(PaintFragment.newInstance())
            }

            btnCanvas.setOnClickListener {
                Log.d(TAG, "canvasPage")
                getCommit(CanvasFragment.newInstance())
            }
            btnCamera.setOnClickListener {
                Log.d(TAG, "cameraPage")
                getCommit(CameraFragment.newInstance())
            }

            btnMatrix.setOnClickListener {
                Log.d(TAG, "matrixPage")
                getCommit(MatrixFragment.newInstance())
            }

            btnPractice.setOnClickListener {
                Log.d(TAG, "practicePage")
                getCommit(PracticeFragment.newInstance())
            }

            btnScrollDemo.setOnClickListener {
                Log.d(TAG, "scrollPage")
                getCommit(ScalaImageFragment.newInstance())
            }

            btnHomework.setOnClickListener {
                Log.d(TAG, "homeworkPage")
                getCommit(MultiTouchFragment.newInstance())
            }

            btnNavigate.setOnClickListener {
                Log.d(TAG, "navigate")
                startActivity(Intent(this@MainActivity, ShareElementActivity::class.java))
            }

            btnMotion.setOnClickListener {
                Log.d(TAG, "motion")
                startActivity(Intent(this@MainActivity, MotionLayoutActivity::class.java))
            }

            btnMotionSearch.setOnClickListener {
                Log.d(TAG, "motionSearch")
                startActivity(Intent(this@MainActivity, MotionSearchActivity::class.java))
            }

            btnFloatAction.setOnClickListener {
                floatingShow()
            }

            btnCompose.setOnClickListener {
                Log.d(TAG, "compose")
                startActivity(Intent(this@MainActivity, ComposeActivity::class.java))
            }

            btnRecyclerView.setOnClickListener {
                Log.d(TAG, "recyclerView")
                startActivity(Intent(this@MainActivity, RecyclerViewActivity::class.java))
            }

            // 新增按钮，跳转到MultiViewActivity
            val btnMultiView = androidx.appcompat.widget.AppCompatButton(this@MainActivity).apply {
                id = View.generateViewId()
                text = "MultiView"
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    120
                )
            }
            (btnRecyclerView.parent as android.view.ViewGroup).addView(btnMultiView)
            btnMultiView.setOnClickListener {
                startActivity(Intent(this@MainActivity, MultiViewActivity::class.java))
            }
        }
    }

    private fun getCommit(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.llt_container, fragment).addToBackStack(null).commit()
    }

    private fun floatingShow() {
        Log.d("custom", "floatingShow:$showing")


        if (mWindow == null) {
            mWindow = InstantFloatingWindow
                .with(this@MainActivity)
                .setLogo(R.drawable.floating_logo)
                .setLayoutType(LayoutType.LEFT)
                .setMenuItems(sparseArray)
                .setMenuItemsClickListener { position: Int, title: String -> LogUtils.d("点击了菜单列表中第" + position + "个菜单项,标题为" + title) }
                .build()
        }

        if (showing) {
            mWindow?.hide()
        } else {
            mWindow?.show()
        }
        showing = !showing
    }

    override fun onDestroy() {
        mWindow?.onDestroy()
        super.onDestroy()
    }


}