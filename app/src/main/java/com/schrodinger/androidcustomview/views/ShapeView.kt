package com.schrodinger.androidcustomview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class ShapeView : View {

    private val TAG = "ShapeViewTAG"
    private var currentShape = Shape.Square
    private val paint:Paint = Paint()
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {

    }

    private val squareRectF = RectF()
    private val trianglePath = Path()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when(currentShape){
            Shape.Circle -> {
                paint.color = Color.RED
                canvas?.drawCircle(width/2.0f,height/2.0f,width/2.0f,paint)
            }
            Shape.Square -> {
                paint.color = Color.GREEN
                squareRectF.apply {
                    left = 0.0f
                    top = 0.0f
                    right = width.toFloat()
                    bottom = height.toFloat()
                }
                canvas?.drawRect(squareRectF,paint)
            }
            Shape.Triangle -> {
                paint.color = Color.BLUE
                trianglePath.apply {
                    //这样画是等腰三角形，需要画成等边三角形才好看
//                    val triangleHeight = width
                    //Math.sqrt(3.0) 根号3，开平方3
                    val triangleHeight = (width/2.0 * Math.sqrt(3.0)).toFloat()
                    moveTo(width/2.0f,0.0f)
                    lineTo(width.toFloat(),triangleHeight)
                    lineTo(0.0f,triangleHeight)
                    lineTo(width/2.0f,0.0f)
                    //使路径闭合
//                    close()
                }
                canvas?.drawPath(trianglePath,paint)
            }
        }
    }

    enum class Shape {
        Circle,Square,Triangle
    }

    fun changeShape() {
        when(currentShape) {
            Shape.Circle->{
                currentShape = Shape.Square
            }
            Shape.Square -> {
                currentShape = Shape.Triangle
            }
            Shape.Triangle -> {
                currentShape = Shape.Circle
            }
        }
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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