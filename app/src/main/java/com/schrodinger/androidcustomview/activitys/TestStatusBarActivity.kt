package com.schrodinger.androidcustomview.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import com.schrodinger.androidcustomview.R
import com.schrodinger.androidcustomview.databinding.ActivityTestStatusBarBinding

class TestStatusBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val binding = ActivityTestStatusBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}