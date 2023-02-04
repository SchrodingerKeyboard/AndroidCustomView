package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.HorizontalScrollView

//HorizontalScrollView是怎么实现滑动的 overScroll
class CustomSlidingMenu : HorizontalScrollView {

    private val TAG = "CustomSlidingMenuTAG"
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        Log.d(TAG,"onOverScrolled scrollX:$scrollX\tscrollY:$scrollY\tclampedX:$clampedX\tclampedY:$clampedY")
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    override fun scrollTo(x: Int, y: Int) {
        Log.d(TAG,"scrollTo:x:$x\ty:$y")
        super.scrollTo(x, y)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"onTouchEvent:ev:$x")
        return super.onTouchEvent(ev)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG,"onMeasure:ev widthMeasureSpec:${MeasureSpec.toString(widthMeasureSpec)}\theightMeasureSpec:${MeasureSpec.toString(heightMeasureSpec)}")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.d(TAG,"onLayout changed:$changed\tl:$l\tt:$t\tr:$r\tb:$b")
//        GestureDetector.OnGestureListener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG,"onSizeChanged w:$w\th:$h\toldw:$w\toldh:$oldh")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.d(TAG,"onFinishInflate")
    }
}