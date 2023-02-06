package com.schrodinger.androidcustomview.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.schrodinger.androidcustomview.R
import com.schrodinger.androidcustomview.databinding.ActivityDragViewBinding

class DragViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDragViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}