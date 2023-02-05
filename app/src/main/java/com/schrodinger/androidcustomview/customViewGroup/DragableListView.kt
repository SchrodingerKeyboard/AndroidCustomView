package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper

//https://cloud.tencent.com/developer/article/1383108  这里写得很详细
class DragableListView : FrameLayout {

    private val TAG = "DragableListViewTAG"
    private var menuView: View? = null
    private var dragView: View? = null
    private var viewDragHelper: ViewDragHelper? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        viewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                Log.d(TAG, "tryCaptureView child:$child\tpointerId:$pointerId\tdragView:$dragView")
                return child == dragView
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return top
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            viewDragHelper?.processTouchEvent(this)
        }

        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return viewDragHelper?.shouldInterceptTouchEvent(ev!!)?:false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 2) {
            throw java.lang.IllegalStateException("只能有两个子View，现在有$childCount 个View")
        }
        menuView = getChildAt(0)
        dragView = getChildAt(1)

    }
}