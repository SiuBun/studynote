package com.wsb.customview.motionlayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.wsb.customview.R
import com.wsb.customview.databinding.ActivityMotionSearchBinding
import com.wsb.customview.utils.LogUtils

/**
 * MotionSearch
 *
 * @author : siubun
 * @date : 2024/06/18
 */
class MotionSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_search)
        val motionLayout = findViewById<MotionLayout>(R.id.motion_search)

        val search = findViewById<View>(R.id.iv_search)
        search.post {
            LogUtils.i("search.post")
            motionLayout.transitionToEnd()
        }
        search.setOnClickListener {
            LogUtils.i("iv_search")
            motionLayout.transitionToEnd()
        }

        findViewById<View>(R.id.iv_cancel).setOnClickListener {
            LogUtils.i("iv_cancel")
            motionLayout.transitionToStart()
        }

        findViewById<View>(R.id.iv_content).setOnClickListener {
            LogUtils.i("iv_content")
            motionLayout.setTransitionListener(object :MotionLayout.TransitionListener{
                override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

                }

                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {

                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    if (currentId == R.id.start){
                        onBackPressedDispatcher.onBackPressed()
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {

                }

            })
            motionLayout.transitionToStart()
        }


//        binding = ActivityMotionSearchBinding.inflate(LayoutInflater.from(this)).apply {
//            motionSearch.currentState
//            root.post {
//                motionSearch.transitionToState(R.id.end)
//            }
//            motionSearch.transitionToEnd{
//                LogUtils.i("transitionToEnd")
//            }
//        }
    }
}