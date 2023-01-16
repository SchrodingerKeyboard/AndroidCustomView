package com.schrodinger.androidcustomview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log

class ColorTrackTextView : androidx.appcompat.widget.AppCompatTextView {

    private val TAG = "ColorTrackTextView"
    private var originPaint = Paint()
    private var changePaint = originPaint
    private var originColor:Int = Color.BLACK
    private var changeColor:Int = Color.RED

    private var currentProgress = 0.0f
    private var direction:Direction = Direction.DIRECTION_LEFT

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    enum class Direction {
        DIRECTION_LEFT,DIRECTION_RIGHT
    }

    //初始化画笔
    private fun initPaint() {
        originPaint.apply {
            isAntiAlias = true
            isDither = true
            color = originColor
            textSize = this@ColorTrackTextView.textSize
        }
        changePaint = Paint(originPaint)
        changePaint.color = changeColor
    }

    //onMeasure就不需要自己处理了。直接处理onDraw
    override fun onDraw(canvas: Canvas?) {
        //不需要super.onDraw
        //super.onDraw(canvas)

        if(text?.isNotEmpty() == true) {
            //这是画出一个完整的文字
//            val fontMetrics: Paint.FontMetrics = paint.fontMetrics
//            val baseLine = (height / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom).toInt()
//            canvas?.drawText(text.toString(),0f,baseline.toFloat(),originPaint)

            val middle = (width * currentProgress).toInt()
            Log.d(TAG,"onDraw middle:$middle")
            //根据不同朝向去画字体
            // 根据不同的朝向去画字体
            if (direction == Direction.DIRECTION_LEFT) {
                drawOriginDirectionLeft(canvas, middle);
                drawChangeDirectionLeft(canvas, middle);
            }
            if (direction == Direction.DIRECTION_RIGHT) {
                drawOriginDirectionRight(canvas, middle);
                drawChangeDirectionRight(canvas, middle);
            }
        }
    }

    /**
     * 画朝向右边变色字体
     */
    private fun drawChangeDirectionRight(canvas: Canvas?, middle: Int) {
        drawText(canvas, changePaint, width - middle, width)
    }

    /**
     * 画朝向左边默认色字体
     */
    private fun drawOriginDirectionRight(canvas: Canvas?, middle: Int) {
        drawText(canvas, originPaint, 0, width - middle)
    }

    /**
     * 画朝向左边变色字体
     */
    private fun drawChangeDirectionLeft(canvas: Canvas?, middle: Int) {
        drawText(canvas, changePaint, 0, middle)
    }

    /**
     * 画朝向左边默认色字体
     */
    private fun drawOriginDirectionLeft(canvas: Canvas?, middle: Int) {
        drawText(canvas, originPaint, middle, width)
    }

    private fun drawChange(canvas: Canvas?,middle:Int) {
        drawText(canvas,changePaint,0,middle)
    }

    private fun drawOrigin(canvas: Canvas?,middle:Int) {
        drawText(canvas,originPaint,middle,width)
    }

    private fun drawText(canvas: Canvas?,paint: Paint,startX:Int,endX:Int) {
        Log.d(TAG,"drawText startX:$startX\tendX:$endX")
        //这里canvas?.save() canvas?.restore()这里必须成对出现,这且不能少,不然画不出来效果
        canvas?.save()
        canvas?.clipRect(startX,0,endX,height)
        //取文字范围
        val bounds = Rect()
        paint.getTextBounds(text.toString(),0,text.length,bounds)
        val fontMetrics: Paint.FontMetricsInt = paint.fontMetricsInt
        //计算baseLine
        val fontTotalHeight = fontMetrics.bottom - fontMetrics.top
        val offY = fontTotalHeight/2 - fontMetrics.bottom
        val baseline = (measuredHeight+fontTotalHeight) / 2.0f -offY
        val paintX = measuredWidth/2.0f - bounds.width()/2.0f
        canvas?.drawText(text.toString(),paintX,baseline,paint)
        canvas?.restore()
    }

    /**
     * 设置当前的进度
     *
     * @param currentProgress 当前进度
     */
    fun setCurrentProgress(currentProgress: Float) {
        this.currentProgress = currentProgress
        // 重新绘制
        invalidate()
    }

    /**
     * 设置绘制方向，从右到左或者从左到右
     *
     * @param direction 绘制方向
     */
    fun setDirection(direction: Direction) {
        this.direction = direction
    }

    fun setOriginColor(color:Int) {
        originColor = color
    }

    fun setChangeColor(color: Int) {
        changeColor = color
    }
}