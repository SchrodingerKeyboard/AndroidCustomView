package com.schrodinger.androidcustomview.testInterceptTouchEvent

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
                name = typedArray.getString(R.styleable.MyView_name)
                typedArray.recycle()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d()
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}