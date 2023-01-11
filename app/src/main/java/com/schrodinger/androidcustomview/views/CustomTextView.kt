package com.schrodinger.androidcustomview.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.Date

class CustomTextView : View {

    val TAG = "CustomTextView"
    //代码里new的时候调用
    constructor(context: Context?) : super(context) {
        Log.d(TAG,"constructor 1 ${Date().time}")
    }

    /**
     * 在xml布局文件中使用时自动调用
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        Log.d(TAG,"constructor 2 ${Date().time}")
    }

    /**
     * 不会自动调用，如果有默认style时，在第二个构造函数中调用
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        Log.d(TAG,"constructor 3 ${Date().time}")
    }

    /**
     * 只有在API版本>21时才会用到
     * 不会自动调用，如果有默认style时，在第二个构造函数中调用
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        Log.d(TAG,"constructor 4 ${Date().time}")
    }

    /**
     * 自定义View的测量方法
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //布局的宽高都是由这个方法指定
        //指定控件的宽高，需要测量
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        Log.d(TAG,"widthMeasureSpec toString:${MeasureSpec.toString(widthMeasureSpec)}")
        Log.d(TAG,"heightMeasureSpec toString:${MeasureSpec.toString(heightMeasureSpec)}")

        Log.d(TAG,"widthMeasureSpec getSize:${MeasureSpec.getSize(widthMeasureSpec)}")
        Log.d(TAG,"heightMeasureSpec getSize:${MeasureSpec.getSize(heightMeasureSpec)}")
//        var heightMeasureSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2,MeasureSpec.AT_MOST)
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}