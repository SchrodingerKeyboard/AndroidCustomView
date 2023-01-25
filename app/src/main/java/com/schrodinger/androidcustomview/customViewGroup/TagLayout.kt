package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.schrodinger.androidcustomview.R

class TagLayout : ViewGroup {
    private val TAG = "TagLayoutTAG"
    private val rowViews = mutableListOf<MutableList<View>>()
    private var intervalWidth:Int = 0
    private var intervalHeight:Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        context?.let { _context ->
            attrs?.let { _attrs ->
                val typedArray = _context.obtainStyledAttributes(_attrs,R.styleable.TagLayout)
                intervalHeight = typedArray.getDimensionPixelSize(R.styleable.TagLayout_intervalHeight,intervalHeight)
                intervalWidth = typedArray.getDimensionPixelSize(R.styleable.TagLayout_intervalWidth,intervalWidth)
                typedArray.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(
            TAG,
            "onMeasure widthMeasureSpec:${MeasureSpec.toString(widthMeasureSpec)}\theightMeasureSpec:${
                MeasureSpec.toString(heightMeasureSpec)
            }"
        )

        val count = childCount
        Log.d(TAG, "count:$count")
        var width = 0
        var height = 0
        var totalWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd
        rowViews.clear()
        var rows = mutableListOf<View>()
        rowViews.add(rows)
        (0 until count).forEach {
            val childView = getChildAt(it)
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            width += childView.measuredWidth
            //只有一行时，就要开始计算高度，每一次换行就累加一次高度
            if (height == 0) height = childView.measuredHeight + paddingTop + paddingBottom
            if (width > totalWidth) {//需要换行
                Log.d(TAG, "换行，在第${it + 1} 个控件时")
                rows = mutableListOf()
                rows.add(childView)
                rowViews.add(rows)
                width = 0
                totalWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd
                height += childView.measuredHeight+intervalHeight
            } else {
                totalWidth -= intervalWidth
                rows.add(childView)
            }
        }
        Log.d(TAG, "setMeasuredDimension width:$totalWidth\theight:$height\ttotalWidth:$totalWidth\trowViews:$rowViews")
        setMeasuredDimension(totalWidth, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "onLayout changed:$changed\tl:$l\tt:$t\tr:$r\tb:$b")
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        rowViews.forEachIndexed { rowIndex, views ->
            top += if (rowIndex == 0) 0 else views[0].measuredHeight + intervalHeight
            left = paddingStart
            views.forEachIndexed { index, view ->
                left += if (index == 0) 0 else view.measuredWidth + intervalWidth
                right = left + view.measuredWidth
                bottom = top + view.measuredHeight
                Log.d(TAG, "rowIndex:$rowIndex\tindex:$index\tleft:$left\ttop:$top\tright:$right\tbottom:$bottom")
                view.layout(left, top, right, bottom)
            }
        }
    }
}