package com.schrodinger.androidcustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.schrodinger.androidcustomview.databinding.ActivityMainBinding
import com.schrodinger.androidcustomview.views.CustomTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickText.setOnClickListener {
            CustomTextView(context = this@MainActivity)
        }
    }
}