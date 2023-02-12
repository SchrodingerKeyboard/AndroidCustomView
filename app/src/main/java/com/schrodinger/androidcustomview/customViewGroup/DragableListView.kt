package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import androidx.core.widget.ListViewCompat
import androidx.customview.widget.ViewDragHelper

class DragableListView : FrameLayout {

    private val TAG = "DragableListViewTAG"
    private lateinit var menuView: View
    private lateinit var dragableView: View
    private var viewDragHelper: ViewDragHelper? = null
    private var isMenuOpened = false
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
        viewDragHelper = ViewDragHelper.create(this, 1000f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return dragableView == child
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                //先限制垂直拖动距离。上不能为负，下不能多过菜单的高度。
                var dy = top
                var maxHeight = menuView.height
                return if (dy > maxHeight) maxHeight else if (dy < 0) 0 else dy
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                //松开手时，菜单要么全展开，要么全关闭
                val menuHeight = menuView.height
                if (releasedChild.top * 2 > menuHeight) {
                    //展开
                    viewDragHelper?.smoothSlideViewTo(releasedChild, 0, menuHeight)
                    isMenuOpened = true
                } else {
                    //关闭
                    viewDragHelper?.smoothSlideViewTo(releasedChild, 0, 0)
                    isMenuOpened = false
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
        //Log.d(TAG, "getDragableViewFirstViewTop:${getDragableViewFirstViewTop()}\tdispatchTouchEvent event:$ev")
        //不要在这里判断是否需要拦截，直接在onInterceptTouchEvent里拦截。

//        if(!canDragUp() && !canDragDown() && getDragableViewFirstViewTop()>=0 && ev?.action == MotionEvent.ACTION_MOVE) {
//            return true
//        }
        return super.dispatchTouchEvent(ev)
    }


    //可以参考 SwipeRefreshLayout 的canChildScrollUp的实现。
    private var downY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(isMenuOpened) return true
        //这里一直滚动会让List无法滚动，要想办法适当时拦截，适当时放开。
        var scrollY = 0f
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            downY = ev.y
            //DOWN时，实际return的是false,不会执行自身的onTouchEvent，所以需要调用一次processTouchEventss
            viewDragHelper?.processTouchEvent(ev)
        } else if (ev?.action == MotionEvent.ACTION_MOVE) {
            //向下
            scrollY = ev.y - downY
            downY = ev.y
            if (scrollY > 0 && !canChildScrollUp()) {
                return true
            }
        }
        Log.d(TAG, "onInterceptTouchEvent downY:$downY\tscrollY:$scrollY\tcanDragUp:${canDragUp()}\tcanDragDown:${canDragDown()}\tgetDragableViewFirstViewTop:${getDragableViewFirstViewTop()}")
        return super.onInterceptTouchEvent(ev)
    }

    private fun canChildScrollUp(): Boolean {
        return if (dragableView is ListView) {
            ListViewCompat.canScrollList((dragableView as ListView?)!!, -1)
        } else dragableView.canScrollVertically(-1)
    }
    private fun canDragUp(): Boolean {
        if (dragableView.top > 0) {
            return true
        }
        return false
    }

    private fun canDragDown(): Boolean {
        val listView = (dragableView as? ListView)
        listView?.run {
            return firstVisiblePosition == 0 && getChildAt(0).top >= 0
        }
        return false
    }

    private fun getDragableViewFirstViewTop(): Int {
        return (dragableView as? ViewGroup)?.getChildAt(0)?.top ?: 0
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        menuView = getChildAt(0)
        dragableView = getChildAt(1)
    }
}