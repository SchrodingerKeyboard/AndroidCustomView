package com.schrodinger.androidcustomview.activitys.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TranslationBehavior: FloatingActionButton.Behavior {

    private val TAG = "TranslationBehaviorGAG"
    /**
     * 当前是否是显示状态
     */
    private var mIsShow = false
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        Log.d(TAG,"onStartNestedScroll coordinatorLayout:$coordinatorLayout\tchild:$child\tdirectTargetChild:$directTargetChild\ttarget:$target\taxes:$axes\ttype:$type")
        // nestedScrollAxes 滑动关联的轴，我们只关心垂直的滑动
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes,type);
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        Log.d(TAG,"onNestedScroll coordinatorLayout:$coordinatorLayout\tchild:$child\ttarget:$target\tdxConsumed:$dxConsumed\tdyConsumed:$dyConsumed\tdxUnconsumed:$dxUnconsumed\tdyUnconsumed:$dyUnconsumed\ttype:$type\tconsumed:$consumed")
        // 根据情况执行动画 一个显示 一个是影藏
        if (dyConsumed > 0) {
            if (!mIsShow) {
                val params = child.layoutParams as CoordinatorLayout.LayoutParams
                child.animate().translationY((params.bottomMargin + child.measuredHeight).toFloat()).setDuration(400).start()
                mIsShow = !mIsShow
            }
        }

        if (dyConsumed < 0) {
            if (mIsShow) {
                child.animate().translationY(0f).setDuration(400).start()
                mIsShow = !mIsShow
            }
        }
    }
}