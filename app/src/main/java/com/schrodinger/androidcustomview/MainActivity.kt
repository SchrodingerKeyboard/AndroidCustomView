package com.schrodinger.androidcustomview

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
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

        val valueAnimator = ValueAnimator.ofFloat(100f)
        valueAnimator.duration = 5*1000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            Log.d("MainActivity","animatedValue:$value")
            binding.qqStepView.setCurProgressStep(value)
        }
        valueAnimator.start()
    }
}