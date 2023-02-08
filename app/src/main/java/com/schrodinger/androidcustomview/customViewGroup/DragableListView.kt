package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import androidx.customview.widget.ViewDragHelper

class DragableListView : FrameLayout {

    private val TAG = "DragableListViewTAG"
    private var menuView: View? = null
    private var dragableView: View? = null
    private var viewDragHelper: ViewDragHelper? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context, attrs, defStyleAttr, 0
    )

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    ) {
        viewDragHelper = ViewDragHelper.create(this,1000f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return dragableView == child
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                //先限制垂直拖动距离。上不能为负，下不能多过菜单的高度。
                var dy = top
                var maxHeight = menuView?.height ?: 0
                return if (dy > maxHeight) maxHeight else if (dy < 0) 0 else dy
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                //松开手时，菜单要么全展开，要么全关闭
                val menuHeight = menuView?.height?:0
                if(releasedChild.top*2 > menuHeight) {
                    //展开
                    viewDragHelper?.smoothSlideViewTo(releasedChild,0,menuHeight)
                } else {
                    //关闭
                    viewDragHelper?.smoothSlideViewTo(releasedChild,0,0)
                }
                //必须调用这一行和continueSettling配合。
                invalidate()
            }
        })
    }

    override fun computeScroll() {
        super.computeScroll()
        //直到返回false
        if (viewDragHelper?.continueSettling(true) == true) {
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged w:$w\t")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent event:$event")
        event?.run {
            viewDragHelper?.processTouchEvent(this)
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "dispatchTouchEvent event:$ev")
        //不要在这里判断是否需要拦截，直接在onInterceptTouchEvent里拦截。
        return super.dispatchTouchEvent(ev)
    }


    //可以参考 SwipeRefreshLayout 的canChildScrollUp的实现。
    private var downY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //这里一直滚动会让List无法滚动，要想办法适当时拦截，适当时放开。
        if(ev?.action == MotionEvent.ACTION_DOWN) {
            downY = ev.y
            //DOWN时，实际return的是false,不会执行自身的onTouchEvent，所以需要调用一次processTouchEventss
            viewDragHelper?.processTouchEvent(ev)
        } else if(ev?.action == MotionEvent.ACTION_MOVE) {
            //向下
            if(ev.y-downY>0) {
                dragableView?.let {
                    if(it is ListView && it.firstVisiblePosition == 0 && it.getChildAt(0).top>=0) {
                        return true
                    }
                }
            } else {
                //向上
                dragableView?.let {
                    if(it.top>0) {
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        menuView = getChildAt(0)
        dragableView = getChildAt(1)
    }
}