package com.schrodinger.androidcustomview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.schrodinger.androidcustomview.R
import com.schrodinger.androidcustomview.extension.getBaseLine

class LetterSideBar : View {

    private val TAG = "LetterSideBarTAG"

    private var letterNormalColor: Int = Color.BLACK
    private var letterSelectedColor: Int = Color.RED
    private var letterTextSize: Float = 12.0f
    private val textPaint = Paint().apply {
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    private var selectedPosition: Int = -1
    private val letterList = mutableListOf<Char>().apply {
        addAll('A'..'Z')
        add('#')
    }.toList()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        context?.let { _context ->
            attrs?.let { _attrs ->
                initAttrs(_context, _attrs)
            }
        }
    }

    private var itemHeight: Float = 0.0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (itemHeight == 0.0f)
            itemHeight = measuredHeight.toFloat() / letterList.size
        letterList.forEachIndexed { index, char ->
            val text = char.toString()
            val textWidth = textPaint.measureText(text)
            val start = (measuredWidth - textWidth) / 2.0f
            //这里效果更好的话，可以先画一个圆形背景
            textPaint.color = if (index == selectedPosition) letterSelectedColor else letterNormalColor
            canvas?.drawText(text, start, itemHeight * index + textPaint.getBaseLine(itemHeight / 2), textPaint)
        }
    }

    private fun getPosition(x: Float): Int {
        (0..letterList.size).forEach { index ->
            val start = itemHeight * index
            val end = start + itemHeight
            if (x in (start..end)) return index
        }
        return letterList.size -1
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent event:$event")
        when (event?.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                val tempSelectedPosition = getPosition(event.y)
                if (tempSelectedPosition != selectedPosition) {
                    selectedPosition = tempSelectedPosition
                    invalidate()
                    //这里可能通过接口回调给外部View通知
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                if (selectedPosition != -1) {
                    selectedPosition = -1
                    invalidate()
                    //这里可能通过接口回调给外部View通知
                }
            }
            else -> {}
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //所有字母的宽度都是一样的
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        letterList.forEach {
//            val  textWidth = textPaint.measureText(it.toString())
//            Log.d(TAG,"$it textWidth:$textWidth")
//        }
        //从上面可知，最大宽度的字是W
        val textWidth = textPaint.measureText("W")
        Log.d(TAG, "textWidth:$textWidth")

        setMeasuredDimension(textWidth.toInt() + paddingStart + paddingEnd, MeasureSpec.getSize(heightMeasureSpec))
    }

    private fun initAttrs(_context: Context, _attrs: AttributeSet) {
        val typedArray = _context.obtainStyledAttributes(_attrs, R.styleable.LetterSideBar)
        letterNormalColor = typedArray.getColor(R.styleable.LetterSideBar_letterNormalColor, letterNormalColor)
        letterSelectedColor = typedArray.getColor(R.styleable.LetterSideBar_letterSelectedColor, letterSelectedColor)
        letterTextSize = typedArray.getDimension(R.styleable.LetterSideBar_letterTextSize, letterTextSize)
        textPaint.run {
            textSize = letterTextSize
            color = letterNormalColor
            isAntiAlias = true
        }
        typedArray.recycle()
    }
}