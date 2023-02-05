package com.schrodinger.androidcustomview.activitys

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat

open class BaseSkinActivity : AppCompatActivity() {

    private val TAG = "BaseSkinActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(this@BaseSkinActivity)
        Log.d(TAG, "factory2:${layoutInflater.factory2}")
        Log.d(TAG, "factory:${layoutInflater.factory}")
        //这样就会修改了默认的Factory2,
        // setFactory2内部会抛异常，限制只能设置一次，所以这里放在前面super.onCreate(savedInstanceState)
        //因为super.onCreate(savedInstanceState)已经判断了，不会重复设置，不会抛异常。
        LayoutInflaterCompat.setFactory2(layoutInflater, object : Factory2 {

            override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
                //拦截并自定义创建一个View返回。
                return if ("Button".equals(name)) {
                    Button(context, attrs).apply {
                        setTextColor(Color.BLUE)
                        isAllCaps = false
                    }
                } else null
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return onCreateView(null, name, context, attrs)
            }
        })
        super.onCreate(savedInstanceState)
    }
}