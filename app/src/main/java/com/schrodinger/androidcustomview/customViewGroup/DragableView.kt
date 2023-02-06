package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper

//https://cloud.tencent.com/developer/article/1383108  这里写得很详细
class DragableView : FrameLayout {

    private val TAG = "DragableListViewTAG"
    private var menuView: View? = null
    private var dragView: View? = null
    private var flingCapturedView: View? = null
    private var btnSmoothSlideView: View? = null
    private var viewDragHelper: ViewDragHelper? = null
    private var onEdgeDragStarted: Boolean = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        viewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            var dragOriginalLeft = 0
            var dragOriginalTop = 0
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                Log.d(TAG, "tryCaptureView child:$child\tpointerId:$pointerId\tdragView:$dragView")
                return child == dragView || onEdgeDragStarted || flingCapturedView == child
                //开启边缘触发时，不在这里限制View，而是在 onEdgeDragStarted里通过 viewDragHelper?.captureChildView(childView,pointerId)方法指定
            }

            //可以横向滑动的距离
            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                Log.d(TAG, "clampViewPositionHorizontal child:$child\tleft:$left\tdx:$dx")
                return left
            }

            //可以纵向滑动的距离
            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                Log.d(TAG, "clampViewPositionVertical child:$child\ttop:$top\tdy:$dy")
                return top
            }

            override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
                super.onViewCaptured(capturedChild, activePointerId)
                Log.d(TAG, "onViewCaptured activePointerId:$activePointerId")
                dragOriginalLeft = capturedChild.left
                dragOriginalTop = capturedChild.top
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                Log.d(TAG, "onViewReleased releasedChild:$releasedChild\txvel:$xvel\tyvel:$yvel")

                if (flingCapturedView == releasedChild) {
                    Log.d(TAG, "flingCapturedView")
                    //内部就是对mCapturedView的操作，所以这个方法只能对按下拖动的View有效。
                    //快速滑动，实际就是松手后会惯性滑动一段距离
                    viewDragHelper?.flingCapturedView(
                        paddingLeft, paddingTop,
                        width - paddingRight - releasedChild.width,
                        height - paddingBottom - releasedChild.height
                    )
                } else {
                    Log.d(TAG, "settleCapturedViewAt")
                    //把View定位回某处，这里是原处
                    viewDragHelper?.settleCapturedViewAt(dragOriginalLeft, dragOriginalTop)
                }
                //重绘。
                invalidate()
            }

            //边缘触发,这个边缘，指的是ViewGroup的边缘（四条边），而不是屏幕的边缘。
            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                super.onEdgeDragStarted(edgeFlags, pointerId)
                Log.d(TAG, "onEdgeDragStarted edgeFlags:$edgeFlags\tpointerId:$pointerId")
                dragView?.let {
                    viewDragHelper?.captureChildView(it, pointerId)
                }
            }

            override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
                super.onEdgeTouched(edgeFlags, pointerId)
                Log.d(TAG, "onEdgeTouched edgeFlags:$edgeFlags\tpointerId:$pointerId")
            }

            override fun onEdgeLock(edgeFlags: Int): Boolean {
                return super.onEdgeLock(edgeFlags)
                Log.d(TAG, "onEdgeLock edgeFlags:$edgeFlags")
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                //重写返回1，为的是让Button这些能消费Touch事件的View也能拖动
                return 1
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return 1
            }
        })
        viewDragHelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)
    }

    override fun computeScroll() {
        super.computeScroll()
        //直到返回false
        if (viewDragHelper?.continueSettling(true) == true) {
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            viewDragHelper?.processTouchEvent(this)
        }

        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return viewDragHelper?.shouldInterceptTouchEvent(ev!!) ?: false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
//        if (childCount != 2) {
//            throw java.lang.IllegalStateException("只能有两个子View，现在有$childCount 个View")
//        }
        menuView = getChildAt(0)
        dragView = getChildAt(1)
        flingCapturedView = getChildAt(2)
        btnSmoothSlideView = getChildAt(3)
        btnSmoothSlideView?.setOnClickListener {

            flingCapturedView?.let {
                var top = it.top
                if (top <= 0) {
                    top = this@DragableView.height - paddingBottom - it.height
                } else {
                    top = 0
                }
                Log.d(TAG, "top:$top\tbtnSmoothSlideView:$btnSmoothSlideView\tflingCapturedView:$flingCapturedView")
                //快速移动到某个地方。
                viewDragHelper?.smoothSlideViewTo(it, 0, top)
                invalidate()
            }
        }
    }
}