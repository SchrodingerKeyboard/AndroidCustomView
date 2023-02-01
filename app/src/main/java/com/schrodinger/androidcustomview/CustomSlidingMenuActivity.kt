package com.schrodinger.androidcustomview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.schrodinger.androidcustomview.databinding.ActivityCustomSlidingMenuBinding

class CustomSlidingMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCustomSlidingMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}