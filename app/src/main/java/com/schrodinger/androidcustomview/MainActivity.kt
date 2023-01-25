package com.schrodinger.androidcustomview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.schrodinger.androidcustomview.databinding.ActivityMainBinding
import com.schrodinger.androidcustomview.views.ColorTrackTextView
import com.schrodinger.androidcustomview.views.CustomTextView


class MainActivity : BaseSkinActivity()/*Activity()*/ {
    private val TAG = "MainActivityTAG";
    private var colorTrackTextView:ColorTrackTextView? = null
    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Log.d(TAG,"height:${binding?.colorTrackTextView?.measuredHeight}")
        binding?.root?.post {
            Log.d(TAG,"height2:${binding?.colorTrackTextView?.measuredHeight}")
        }
        colorTrackTextView = binding?.colorTrackTextView
        binding?.clickText?.setOnClickListener {
            CustomTextView(context = this@MainActivity)
        }

        playAnim(binding)
        binding?.clickPlayAnim?.setOnClickListener {
            playAnim(binding)
        }

        binding?.clickText?.setOnClickListener {
            binding?.qqStepView?.invalidate()
        }
        binding?.clickLeft?.setOnClickListener {
            left()
        }
        binding?.clickRight?.setOnClickListener {
            right()
        }
        binding?.colorTrackActivity?.setOnClickListener {
            startActivity(Intent(this@MainActivity,ViewPagerColorTrackTextViewActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"height3:${binding?.colorTrackTextView?.measuredHeight}")
    }

    private fun playAnim(binding: ActivityMainBinding?) {
        binding?.qqStepView?.clearAnimation()
        binding?.customProgressBar?.clearAnimation()
        val valueAnimator = ValueAnimator.ofFloat(100f)
        valueAnimator.duration = 5 * 1000
        valueAnimator.interpolator = LinearInterpolator()
        var oldValue = 0.0f
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
//            Log.d(TAG, "animatedValue:$value")
            binding?.qqStepView?.setCurProgressStep(value)
            binding?.customProgressBar?.setCurrentProgress(value.toInt())
            if(value - oldValue > 10) {
                oldValue = value
                binding?.shapeView?.changeShape()
            }
        }
        valueAnimator.start()
    }

    // onClick事件写在了不居中  --> android:onClick="left"
    fun left() {
        // 设置朝向
        colorTrackTextView?.run {
            setDirection(ColorTrackTextView.Direction.DIRECTION_LEFT)
            // 用属性动画来控制，当然也可以用线程去控制
            val animator: ValueAnimator = ObjectAnimator.ofFloat(0f, 1f)
            animator.setDuration(2000).start()
            // 添加动画的监听，不断的改变当前的进度
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                Log.e(TAG, "progress --> $progress")
                colorTrackTextView?.setCurrentProgress(progress)
            }
        }
    }

    // 这与上面类似，只是朝向不一样
    fun right() {
        colorTrackTextView?.run {
            setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT)
            val animator: ValueAnimator = ObjectAnimator.ofFloat( 0f, 1f)
            animator.setDuration(2000)
                .start()
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                Log.e(TAG, "progress --> $progress")
                setCurrentProgress(progress)
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}