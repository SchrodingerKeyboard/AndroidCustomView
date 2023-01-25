package com.schrodinger.androidcustomview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.schrodinger.androidcustomview.databinding.ActivityViewPagerColorTrackTextViewBinding
import com.schrodinger.androidcustomview.customViews.ColorTrackTextView


class ViewPagerColorTrackTextViewActivity : AppCompatActivity() {

    private val TAG = "ViewPagerColorTrackTAG"
    private val items = arrayOf("直播", "推荐", "视频", "图片", "段子", "精华")
    private val mIndicators: MutableList<ColorTrackTextView> = mutableListOf()
    private var indicatorContainer:LinearLayout? = null
    private var viewPager:ViewPager2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityViewPagerColorTrackTextViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        indicatorContainer = binding.indicatorContainer
        viewPager = binding.viewPager2
        initIndicator()
        initViewPager()
    }

    private fun initViewPager() {
        val adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return object : ViewHolder(TextView(this@ViewPagerColorTrackTextViewActivity)) {
                }
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                Log.d(TAG, "onBindViewHolder holder:$holder\tposition:$position")
                holder.itemView.layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.MATCH_PARENT
                    )
                )
                (holder.itemView as TextView).apply {
                    text = items.get(position)
                    setTextColor(Color.WHITE)
                }
            }

            override fun getItemCount(): Int {
                return items.size
            }
        }
        viewPager?.adapter = adapter
        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.e(TAG, "position --> $position positionOffset --> $positionOffset")
                if (positionOffset > 0) {
                    // 获取左边
                    val left = mIndicators[position]
                    // 设置朝向
                    left.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT)
                    // 设置进度  positionOffset 是从 0 一直变化到 1 不信可以看打印
                    left.setCurrentProgress(1 - positionOffset)
                    if(position <=mIndicators.size-1) {
                        // 获取右边
                        val right = mIndicators[position + 1]
                        right.setDirection(ColorTrackTextView.Direction.DIRECTION_LEFT)
                        right.setCurrentProgress(positionOffset)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        // 默认一进入就选中第一个
        val left = mIndicators[0]
        left.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT)
        left.setCurrentProgress(1f)
    }

    /**
     * 初始化可变色的指示器
     */
    private fun initIndicator() {
        for (i in 0 until items.size) {
            // 动态添加颜色跟踪的TextView
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.weight = 1f
            val colorTrackTextView = ColorTrackTextView(this)
            // 设置两种颜色
            colorTrackTextView.setOriginColor(Color.BLACK)
            colorTrackTextView.setChangeColor(Color.RED)
            colorTrackTextView.text = items[i]
            colorTrackTextView.layoutParams = params
            // 把新的加入LinearLayout容器
            indicatorContainer?.addView(colorTrackTextView)
            // 加入集合
            mIndicators.add(colorTrackTextView)
        }
    }
}