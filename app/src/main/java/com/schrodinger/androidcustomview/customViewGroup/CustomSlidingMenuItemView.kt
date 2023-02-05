package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout

//https://blog.csdn.net/dakun012/article/details/79674012
class CustomSlidingMenuItemView :LinearLayout {

    private val TAG = "CustomSlidingItemTAG"
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        orientation = LinearLayout.VERTICAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG,"widthMeasureSpec:${MeasureSpec.toString(widthMeasureSpec)}\theightMeasureSpec:${MeasureSpec.toString(heightMeasureSpec)}")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG,"onLayout changed:$changed")
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG,"onDraw canvas:$canvas")
        super.onDraw(canvas)
    }

    override fun scrollTo(x: Int, y: Int) {
        Log.d(TAG,"scrollTo x:$x\ty:$y")
        super.scrollTo(x, y)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        Log.d(TAG,"onScrollChanged l:$l\tt:$t\toldl:$oldt:$oldt")
        super.onScrollChanged(l, t, oldl, oldt)
    }
}