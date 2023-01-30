package com.schrodinger.androidcustomview.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.schrodinger.androidcustomview.R

class CustomProgressBar : View {

    private val TAG = "CustomProgressBarTAG"
    private var innerColor: Int = Color.RED
    private var outerColor: Int = Color.BLUE
    private var progressTextColor: Int = 10
    //单位最好都使用float，因为精度高
    private var borderWidth: Float = 10.0f
    private var progressTextSize: Float = 12.0f

    private var innerPaint: Paint = Paint()
    private var outerPaint: Paint = innerPaint
    private var progressTextPaint = Paint()
    private var maxProgress: Int = 100
    private var currentProgress: Int = 50

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        attrs?.let { _attrs ->
            context?.let { _context ->
                initAttr(_context, _attrs)
            }
        }
        initPaint()
    }

    private fun initPaint() {
        innerPaint.apply {
            color = innerColor
            isAntiAlias = true
            strokeWidth = borderWidth.toFloat()
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            style = Paint.Style.STROKE
        }
        outerPaint = Paint(innerPaint)
        outerPaint.color = outerColor

        progressTextPaint.apply {
            color = progressTextColor
            textSize = progressTextSize
            isAntiAlias = true
        }
    }

    private val textBounds = Rect()
    private val rectF = RectF(

    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val halfBorderWidth = borderWidth / 2
        //画外圆 注意要在这里开始转成float，不要先转成int,再toFloat,不然会丢失精度，到时候就会造成两个圆差几个px的问题。
        val cx = width / 2.0f
        val cy = height / 2.0f
        val radius = width / 2.0f - halfBorderWidth
        canvas?.drawCircle(cx, cy, radius, innerPaint)

        //画外圆
        rectF.apply {
            left = halfBorderWidth
            top = halfBorderWidth
            right = width - halfBorderWidth
            bottom = height - halfBorderWidth
        }
        if (currentProgress > 0) {
            canvas?.drawArc(rectF, 0f, currentProgress/maxProgress.toFloat() * 360f, false, outerPaint)
        }
//        Log.d(TAG, "width:$width\theight:$height\tcx:$cx\tcy:$cy\tradius:$radius\thalfBorderWidth:$halfBorderWidth\tcurrentProgress:$currentProgress\tmaxProgress:$maxProgress")

        //画文本
        val text = "${((currentProgress / maxProgress.toFloat()) * 100).toInt().toString()}%"
        progressTextPaint.getTextBounds(text, 0, text.length, textBounds)
        val dx = width / 2.0f - textBounds.width() / 2.0f
        // 获取画笔的FontMetrics
        val fontMetrics: Paint.FontMetrics = progressTextPaint.getFontMetrics()
        // 计算文字的基线
        val baseLine = (height / 2.0f + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom)
        canvas?.drawText(text, dx, baseLine, progressTextPaint)

    }

    private fun initAttr(_context: Context, _attrs: AttributeSet) {
        val typedArray = _context.obtainStyledAttributes(_attrs, R.styleable.CustomProgressBar)
        innerColor = typedArray.getColor(R.styleable.CustomProgressBar_progressInnerColor, innerColor)
        outerColor = typedArray.getColor(R.styleable.CustomProgressBar_progressOuterColor, outerColor)

        progressTextColor = typedArray.getColor(R.styleable.CustomProgressBar_progressTextColor, progressTextColor)
        borderWidth = typedArray.getDimension(R.styleable.CustomProgressBar_progressBorderWidth, borderWidth)
        progressTextSize =
            typedArray.getDimension(R.styleable.CustomProgressBar_progressTextSize, progressTextSize)

        val progressTextSizeDimension = typedArray.getDimension(R.styleable.CustomProgressBar_progressTextSize, 0f)
        val progressTextSizeDimensionPixelSize =
            typedArray.getDimensionPixelSize(R.styleable.CustomProgressBar_progressTextSize, 0)
        Log.d(TAG, "progressTextSize getDimension:${progressTextSizeDimension}")
        Log.d(TAG, "progressTextSize getDimensionPixelSize:${progressTextSizeDimensionPixelSize}")
        typedArray.recycle()
    }

    private fun dip2px(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
    }

    private fun sp2px(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics).toInt()
    }

    fun setMax(max:Int) {
        maxProgress = max
    }

    fun setCurrentProgress(current:Int) {
        currentProgress = current
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //宽度和高度不一致，取最小值，确保是一个正方形
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val value = if (width > height) height else width
        Log.d(
            TAG,
            "onMeasure widthMeasureSpec:${MeasureSpec.toString(widthMeasureSpec)}\theightMeasureSpec:${
                MeasureSpec.toString(heightMeasureSpec)
            }\twidth:$width\theight:$height"
        )
        setMeasuredDimension(value, value)
    }
}