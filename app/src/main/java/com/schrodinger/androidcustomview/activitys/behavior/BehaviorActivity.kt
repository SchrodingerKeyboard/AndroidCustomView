package com.schrodinger.androidcustomview.activitys.behavior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.schrodinger.androidcustomview.R
import com.schrodinger.androidcustomview.databinding.ActivityBehaviorBinding
//https://blog.csdn.net/whoami_I/article/details/103270894/
//CollapsingToolbarLayout
class BehaviorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBehaviorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.run {
            initRecyclerView()
        }
        binding.fab.setOnClickListener {
            Toast.makeText(this@BehaviorActivity,"FloatingActionButton click",Toast.LENGTH_SHORT).show()
            Snackbar.make(binding.root,"FloatingActionButton click",1).show()
        }
        binding.bottomTabLayout.setOnClickListener {
            Snackbar.make(binding.root,"FloatingActionButton click",1).show()
        }
    }

    private fun RecyclerView.initRecyclerView() {
        layoutManager = LinearLayoutManager(this@BehaviorActivity, LinearLayoutManager.VERTICAL, false)
        adapter = object : Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return object : ViewHolder(TextView(this@BehaviorActivity)) {}
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val itemView = holder.itemView
                (itemView as? TextView)?.text = "position:$position"
            }

            override fun getItemCount() = 100
        }
    }
}