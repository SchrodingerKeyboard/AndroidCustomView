package com.schrodinger.androidcustomview.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.schrodinger.androidcustomview.R

class QQStepView : View {

    private val TAG = "QQStepViewTAG"
    private var outerColor: Int = Color.RED
    private var innerColor: Int = Color.BLUE
    private var borderWidth:Float = 20.0f
    private var stepTextSize = 20.0f
    private var stepTextColor = Color.RED

    private val outerPaint = Paint()
    private var innerPaint:Paint = outerPaint
    private var textPaint:Paint = outerPaint

    //外圆Rect,因为四个点是float，所以是叫做RectF
    private val outerOval = RectF()
    private val textBounds = Rect()

    //圆孤开始角度
    private var startAnglel:Float = 135.0f
    //扫过的角度
    private var sweepAngle:Float = 270.0f

    //最大进度
    private var maxStep:Float = 100.0f
    //当前进度
    private var curProgressStep:Float = 60.0f

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
                initAttrs(_context, _attrs)
                initPaint()
            }
        }
    }

    private fun initPaint() {
        // 设置圆弧画笔的宽度
        outerPaint.strokeWidth = borderWidth
        // 设置为 ROUND
        outerPaint.strokeCap = Paint.Cap.ROUND
        //笔触是圆的还是方的。
        outerPaint.strokeJoin = Paint.Join.ROUND
        // 设置画笔颜色
        outerPaint.color = outerColor
        outerPaint.style = Paint.Style.STROKE
        outerPaint.isAntiAlias = true

        innerPaint = Paint(outerPaint)
        innerPaint.color = innerColor
        innerPaint.strokeWidth = borderWidth

        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = stepTextColor
        textPaint.textSize = stepTextSize

    }

    private fun initAttrs(_context: Context, _attrs: AttributeSet) {
        val array = _context.obtainStyledAttributes(_attrs, R.styleable.QQStepView)
        outerColor = array.getColor(R.styleable.QQStepView_outerColor, outerColor)
        innerColor = array.getColor(R.styleable.QQStepView_innerColor, innerColor)
        borderWidth = array.getDimension(R.styleable.QQStepView_borderWidth, borderWidth)
        stepTextSize = array.getDimension(R.styleable.QQStepView_stepTextSize, stepTextSize)
        stepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, stepTextColor)
        array.recycle()
    }

    fun setCurProgressStep(progressStep:Float) {
        curProgressStep = progressStep
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //画外圆
        val centerX = width / 2
        val centerY = height / 2
        //之所以要除2.是因为笔是从中间向两边开始算宽度的。
        val radius = centerX - borderWidth/2
        outerOval.left = centerX - radius
        outerOval.top = centerY-radius
        outerOval.right = centerX+radius
        outerOval.bottom = centerY+radius
        canvas?.drawArc(outerOval,135f,270f,false,outerPaint)

        val percent = curProgressStep / maxStep
        canvas?.drawArc(outerOval,startAnglel,percent*sweepAngle,false,innerPaint)

        val mStep = ((percent * maxStep)).toInt().toString() + ""
        // 测量文字的宽高
        textPaint.getTextBounds(mStep, 0, mStep.length, textBounds)
        val dx: Int = (width - textBounds.width()) / 2
        // 获取画笔的FontMetrics
        val fontMetrics: Paint.FontMetrics = textPaint.getFontMetrics()
        // 计算文字的基线
        val baseLine = (height / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom).toInt()
        // 绘制步数文字
        canvas?.drawText(mStep, dx.toFloat(), baseLine.toFloat(), textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //内部就是调用setMeasuredDimension，后面主动调用了setMeasuredDimension，所以这里可以注释掉
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //处理要xml里没有指定宽高，而是wrap_content
        //获取模式 AT_MOST 默认给40dp

        //宽度和高度不一致，取最小值，确保是一个正方形
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val value = if(width > height) height else width
        Log.d(TAG,"onMeasure widthMeasureSpec:${MeasureSpec.toString(widthMeasureSpec)}\theightMeasureSpec:${MeasureSpec.toString(heightMeasureSpec)}\twidth:$width\theight:$height")
        setMeasuredDimension(value,value)
    }


}