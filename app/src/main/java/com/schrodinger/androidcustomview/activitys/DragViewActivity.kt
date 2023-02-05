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
        binding.listView.adapter = object : BaseAdapter() {
            override fun getCount() = 100

            override fun getItem(position: Int) = position

            override fun getItemId(position: Int) = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val textView = TextView(this@DragViewActivity)
                textView.text = "position:$position"
                return textView
            }
        }

    }
}