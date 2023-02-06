package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper

class DragableListView : FrameLayout {

    private val TAG = "DragableListViewTAG"
    private var menuView: View? = null
    private var dragableView:View? = null
    private var viewDragHelper: ViewDragHelper? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        viewDragHelper = ViewDragHelper.create(this,object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return dragableView == child
            }
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG,"onSizeChanged w:$w\t")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG,"onTouchEvent event:$event")
        event?.run {
            viewDragHelper?.processTouchEvent(this)
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"dispatchTouchEvent event:$ev")
        requestDisallowInterceptTouchEvent(true)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"onInterceptTouchEvent event:$ev")
        return viewDragHelper?.shouldInterceptTouchEvent(ev!!) ?: super.onInterceptTouchEvent(ev)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        menuView = getChildAt(0)
        dragableView = getChildAt(1)
    }
}