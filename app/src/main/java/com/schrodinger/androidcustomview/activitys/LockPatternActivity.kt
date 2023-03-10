package com.schrodinger.androidcustomview.activitys

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.schrodinger.androidcustomview.R

class LockPatternActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_pattern)

        val wm1 = window.windowManager
        Log.d("WINDOW_SERVICE", "window.windowManager:${wm1}")
        val wm2 = getSystemService(Context.WINDOW_SERVICE)
        Log.d("WINDOW_SERVICE", "getSystemService(Context.WINDOW_SERVICE):${wm2}")
        val wm3 = applicationContext.getSystemService(Context.WINDOW_SERVICE)
        Log.d("WINDOW_SERVICE", "applicationContext.getSystemService(Context.WINDOW_SERVICE):${wm3}")
        val wm4 = baseContext.getSystemService(Context.WINDOW_SERVICE)
        Log.d("WINDOW_SERVICE", "baseContext.getSystemService(Context.WINDOW_SERVICE):${wm4}")


    }
}