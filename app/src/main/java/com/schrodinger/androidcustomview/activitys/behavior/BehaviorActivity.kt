package com.schrodinger.androidcustomview.activitys.behavior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schrodinger.androidcustomview.R
import com.schrodinger.androidcustomview.databinding.ActivityBehaviorBinding

class BehaviorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBehaviorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(this@BehaviorActivity,LinearLayoutManager.VERTICAL,false)
            adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    return object : RecyclerView.ViewHolder(TextView(this@BehaviorActivity)){}
                }

                override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                    val itemView = holder.itemView
                    (itemView as? TextView)?.text = "position:$position"
                }

                override fun getItemCount() = 100
            }
        }
    }
}