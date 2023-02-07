package com.schrodinger.androidcustomview.customViewGroup.testInterceptTouchEvent

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.schrodinger.androidcustomview.R

class MyView : View {
    protected var name:String? = null
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        context?.let { _context ->
            attrs?.let { _attrs->
                val typedArray = _context.obtainStyledAttributes(attrs,R.styleable.MyView)
                name = typedArray.getString(R.styleable.MyView_viewName)
                typedArray.recycle()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d(interceptTouchTAG,"before MyView:$name dispatchTouchEvent event:$event")
        val result = super.dispatchTouchEvent(event)
        Log.d(interceptTouchTAG,"after MyView:$name dispatchTouchEvent result:$result\tevent:$event")
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(interceptTouchTAG,"before MyView:$name onTouchEvent event:$event")
        val result = super.onTouchEvent(event)
        Log.d(interceptTouchTAG,"after MyView:$name onTouchEvent result:$result\tevent:$event")
        return result
    }
}