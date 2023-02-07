package com.schrodinger.androidcustomview.customViewGroup.testInterceptTouchEvent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.schrodinger.androidcustomview.R

val interceptTouchTAG = "InterceptTouchTAG"

class InterceptTouchEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intercept_touch_event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(interceptTouchTAG,"before activity dispatchTouchEvent event:$ev")
        val result = super.dispatchTouchEvent(ev)
        Log.d(interceptTouchTAG,"after activity dispatchTouchEvent event:$ev")
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(interceptTouchTAG,"before activity onTouchEvent event:$event")
        val result = super.onTouchEvent(event)
        Log.d(interceptTouchTAG,"after activity onTouchEvent event:$event")
        return result
    }
}