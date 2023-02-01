package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

class TouchView : androidx.appcompat.widget.AppCompatTextView {

    companion object {
        val TAG = "TouchViewTAG"
    }

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG,"TouchView onTouchEvent before event:$event")
        val result = super.onTouchEvent(event)
        Log.d(TAG,"TouchView onTouchEvent result:$result\tevent:$event")
        return result
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG,"TouchView dispatchTouchEvent before event:$event")
        val result = super.dispatchTouchEvent(event)
        Log.d(TAG,"TouchView dispatchTouchEvent result:$result\tevent:$event")
        return result
    }
}