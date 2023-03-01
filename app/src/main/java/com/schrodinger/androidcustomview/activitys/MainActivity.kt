package com.schrodinger.androidcustomview.activitys

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import com.schrodinger.androidcustomview.R
import com.schrodinger.androidcustomview.activitys.behavior.BehaviorActivity
import com.schrodinger.androidcustomview.customViewGroup.TagLayoutAdapter
import com.schrodinger.androidcustomview.customViewGroup.TouchView
import com.schrodinger.androidcustomview.customViewGroup.testInterceptTouchEvent.InterceptTouchEventActivity
import com.schrodinger.androidcustomview.databinding.ActivityMainBinding
import com.schrodinger.androidcustomview.customViews.ColorTrackTextView
import com.schrodinger.androidcustomview.customViews.CustomTextView


class MainActivity : BaseSkinActivity()/*Activity()*/ {
    private val TAG = "MainActivityTAG";
    private var colorTrackTextView: ColorTrackTextView? = null
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Log.d(TAG, "height:${binding?.colorTrackTextView?.measuredHeight}")
        binding?.root?.post {
            Log.d(TAG, "height2:${binding?.colorTrackTextView?.measuredHeight}")
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
            startActivity(Intent(this@MainActivity, ViewPagerColorTrackTextViewActivity::class.java))
        }

        binding?.customSlidingMenu?.setOnClickListener {
            startActivity(Intent(this@MainActivity, CustomSlidingMenuActivity::class.java))
        }

        binding?.testDragViewBtn?.setOnClickListener {

        }

        binding?.testDragViewBtn?.setOnClickListener {
            startActivity(Intent(this@MainActivity, DragViewActivity::class.java))
        }

        binding?.testDragListViewBtn?.setOnClickListener {
            startActivity(Intent(this@MainActivity, DragListViewActivity::class.java))
        }

        binding?.tagAdapterLayout?.run {
            val list = mutableListOf<String>()
            list.add("Hello")
            list.add("World")
            list.add("Kotlin")
            list.add("Java")
            list.add("Android")
            list.add("IOS")
            list.add("ViewGroup")
            list.add("LinearLayout")
            list.add("MainActivity")
            //初始化数据，添加item点击，长按事件
            val adapter = object : TagLayoutAdapter<String>(datas = list, onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Toast.makeText(this@MainActivity, list.get(position), Toast.LENGTH_SHORT).show()
                }
            }) {
                override fun getView(position: Int, parent: ViewGroup): View {
                    val textView = TouchView(parent.context)//LayoutInflater.from(parent.context).inflate(R.layout.)
                    textView.setText(getItem(position))
                    textView.setPadding(10, 0, 10, 0)
                    textView.setTextColor(if (position % 3 == 0) Color.RED else if (position % 3 == 1) Color.BLACK else Color.BLUE)
                    textView.setBackgroundResource(R.drawable.tag_background)
                    return textView
                }
            }
            setAdapter(adapter)
        }

        binding?.testInterceptTouch?.setOnClickListener {
            startActivity(Intent(this@MainActivity, InterceptTouchEventActivity::class.java))
        }
        binding?.testSwipeRefreshLayout?.setOnClickListener {
            startActivity(Intent(this@MainActivity, SwipeRefreshLayoutActivity::class.java))
        }
        binding?.testLockPatternView?.setOnClickListener {
            startActivity(Intent(this@MainActivity, LockPatternActivity::class.java))
        }

        binding?.testStatusBar?.setOnClickListener {
            startActivity(Intent(this@MainActivity, TestStatusBarActivity::class.java))
        }
        binding?.testBehavior?.setOnClickListener {
            startActivity(Intent(this@MainActivity, BehaviorActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "height3:${binding?.colorTrackTextView?.measuredHeight}")
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
            if (value - oldValue > 10) {
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
            val animator: ValueAnimator = ObjectAnimator.ofFloat(0f, 1f)
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TouchView.TAG, "MainActivity dispatchTouchEvent before\tev:$ev")
        val result = super.dispatchTouchEvent(ev)
        Log.d(TouchView.TAG, "MainActivity dispatchTouchEvent result:$result\tev:$ev")
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TouchView.TAG, "MainActivity onTouchEvent before event:$event")
        val result = super.onTouchEvent(event)
        Log.d(TouchView.TAG, "MainActivity onTouchEvent result:$result\tevent:$event")
        return result
    }
}