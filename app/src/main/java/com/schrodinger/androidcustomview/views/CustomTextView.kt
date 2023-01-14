package com.schrodinger.androidcustomview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.Date

class CustomTextView : View {

    private val TAG = "CustomTextView"
    private val textPaint = Paint()
    private val text = "Hello world"

    //代码里new的时候调用
    constructor(context: Context?) : this(context,null) {
        Log.d(TAG,"constructor 1 ${Date().time}")
    }

    /**
     * 在xml布局文件中使用时自动调用
     */
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0) {
        Log.d(TAG,"constructor 2 ${Date().time}")
    }

    /**
     * 不会自动调用，如果有默认style时，在第二个构造函数中调用
     */
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0) {
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
        //初始化 Paint 画笔
        textPaint.apply {
            color = Color.RED
            textSize =  18f
            isAntiAlias = true
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        //某些安卓sdk版本ViewGroup不调用onDraw,可以把逻辑写到这里来，或者处理其他
        Log.d(TAG,"CustomTextView dispatchDraw")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d(TAG,"CustomTextView onDraw")
        val fontMetrics = textPaint.fontMetricsInt
        val dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom
        val baseline = height / 2 + dy
        val x = getPaddingLeft()
        canvas?.drawText(text,x.toFloat(),baseline.toFloat(),textPaint)
    }

    private val textRect = Rect()
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

//算出文本需要占用的宽高
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        textPaint.getTextBounds(text,0,text.length,textRect)
        Log.d(TAG,"Rect width:${textRect.width()}\theight:${textRect.height()}")
        if(widthMode == MeasureSpec.AT_MOST) {
            width = textRect.width()
        }
        if(heightMode == MeasureSpec.AT_MOST) {
            height = textRect.height()
        }
        Log.d(TAG,"setMeasuredDimension width:$width\theight:$height")
        setMeasuredDimension(width,height)
    }
}