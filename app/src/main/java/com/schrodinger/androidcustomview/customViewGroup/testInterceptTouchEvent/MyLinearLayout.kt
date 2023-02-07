package com.schrodinger.androidcustomview.customViewGroup.testInterceptTouchEvent

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import com.schrodinger.androidcustomview.R

class MyLinearLayout : LinearLayout {

    private var name: String? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        context?.let { _context ->
            attrs?.let { _attrs ->
                val typedArray = _context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout)
                name = typedArray.getString(R.styleable.MyLinearLayout_viewGroupName)
                typedArray.recycle()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(interceptTouchTAG,"before ViewGroup:$name dispatchTouchEvent event:$ev")
        val result = super.dispatchTouchEvent(ev)
        Log.d(interceptTouchTAG,"after ViewGroup:$name dispatchTouchEvent event:$ev")
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(interceptTouchTAG,"before ViewGroup:$name onTouchEvent event:$event")
        val result = super.onTouchEvent(event)
        Log.d(interceptTouchTAG,"after ViewGroup:$name onTouchEvent event:$event")
        return result
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(interceptTouchTAG,"before ViewGroup:$name onInterceptTouchEvent event:$ev")
        val result = super.onInterceptTouchEvent(ev)
        Log.d(interceptTouchTAG,"after ViewGroup:$name onInterceptTouchEvent event:$ev")
        return result
    }
}