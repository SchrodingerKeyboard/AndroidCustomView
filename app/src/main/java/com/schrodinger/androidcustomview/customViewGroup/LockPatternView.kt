package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

class LockPatternView : View {

    private val TAG = "LockPatternViewTAG"

    //这几个颜色，必要时，可以做成配置
    private val outerPressedColor = 0xff8cbad8.toInt()
    private val innerPressedColor = 0xff0596f6.toInt()
    private val outerNormalColor = 0xffd9d9d9.toInt()
    private val innerNormalColor = 0xff929292.toInt()
    private val outerErrorColor = 0xff901032.toInt()
    private val innerErrorColor = 0xffea0945.toInt()

    private val drawGesturePointPaint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        //设置画笔为空间
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private val drawLinePaint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        //设置画笔为空间
        style = Paint.Style.FILL
        strokeWidth = 15f
    }

    private val lockPatternPoints = mutableListOf<LockPatternPoint>()
    private val selectedPoints = mutableListOf<LockPatternPoint>()

    private var innerRadius = 15f
    private var outerRadius = 15f

    //密码长度必须大于等于5
    private val passwordLength = 5

    private var lastMotionEvent: MotionEvent? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context, attrs, defStyleAttr, 0
    )

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {

    }

    private fun resetLockPatternPointsState() {
        lockPatternPoints.forEach {
            it.state = LockPointState.Normal
        }
    }

    private fun initLockPatternPoints() {
        //初始化九个点的信息
        lockPatternPoints.clear()
        var index = 0
        (0..2).forEach { row ->
            (0..2).forEach { column ->
                val rowWidth = measuredWidth.toDouble() / 3
                val cx = (row * rowWidth) + rowWidth / 2
                val cy = column * rowWidth + rowWidth / 2
                lockPatternPoints.add(
                    LockPatternPoint(
                        centerX = cx.toFloat(),
                        centerY = cy.toFloat(),
                        index = index++,
                        state = LockPointState.Normal,
                    )
                )
            }
        }
        Log.d(TAG, "lockPatternPoints:$lockPatternPoints")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            //画出手势点
            drawGesturePoint(this)
            //画线
            drawLineBetweenPoints(this)
            //画点与点之间的箭头
            drawArrowsBetweenPoints(this)

        }

    }

    //画九个圆，九宫格的九个点
    fun drawGesturePoint(canvas: Canvas) {

        lockPatternPoints.forEach {
            //半径，线的粗细，必要时可以做成配置。
            //画内圆小圆心，
            drawGesturePointPaint.color =
                if (it.state == LockPointState.Normal) innerNormalColor else if (it.state == LockPointState.Selected) innerPressedColor else innerErrorColor
            canvas.drawCircle(it.centerX, it.centerY, innerRadius, drawGesturePointPaint)
            //画外圆大圆心
            drawGesturePointPaint.color =
                if (it.state == LockPointState.Normal) outerNormalColor else if (it.state == LockPointState.Selected) outerPressedColor else outerErrorColor
            canvas.drawCircle(it.centerX, it.centerY, outerRadius, drawGesturePointPaint)
        }
    }

    fun drawLineBetweenPoints(canvas: Canvas) {
        Log.d(TAG, "drawLineBetweenPoints size:${selectedPoints.size}")
        selectedPoints.forEachIndexed { index, lockPatternPoint ->
            val nextLockPatternPoint: LockPatternPoint? =
                if (index < selectedPoints.size - 1) {
                    selectedPoints[index + 1]
                } else {
                    lastMotionEvent?.run {
                        //这里直接getx,getY是错的。
                        val lastX = getX(actionIndex)
                        val lastY = getY(actionIndex)
                        LockPatternPoint(
                            centerX = lastX,
                            centerY = lastY,
                            index = 9,
                            state = LockPointState.Normal
                        )
                    }
                }


            val startPoint = if (nextLockPatternPoint == null) lockPatternPoint
            else if (lockPatternPoint.centerX < nextLockPatternPoint.centerX || lockPatternPoint.centerY < nextLockPatternPoint.centerY) lockPatternPoint else nextLockPatternPoint
            val endPoint =
                if (startPoint == nextLockPatternPoint) lockPatternPoint else nextLockPatternPoint

            endPoint?.run {
                val dx = startPoint.getDistanceTo(endPoint)
                val offsetX: Double = abs(startPoint.centerX - endPoint.centerX) / (dx / innerRadius)
                val offsetY: Double = abs(startPoint.centerY - endPoint.centerY) / (dx / innerRadius)
                canvas.drawLine(
                    startPoint.centerX + offsetX.toFloat(),
                    startPoint.centerY + offsetY.toFloat(),
                    endPoint.centerX - offsetX.toFloat(),
                    endPoint.centerY - offsetY.toFloat(),
                    drawLinePaint
                )
            }


            if (nextLockPatternPoint == null) {
                //最后一个点到手指的线
//                if(lastX!=null && lastY!=null) {
//                    canvas.drawLine(
//                        lockPatternPoint.centerX,
//                        lockPatternPoint.centerY,
//                        lastX ?: 0f,
//                        lastY ?: 0f,
//                        drawLinePaint
//                    )
//                }
            } else {
                //两个点间的线,不能直接使用centerX/centerY作为起点来画，因为内圆是空心的，要画在内圆的边上，
                // 所以需要计算，计算过程就是根据直角三角形和比例来算，两个点间组成大三角形，内圆圆心到边组成小三角形，角度相同，边长成比例


//                //直角三角形的斜边
//                val dx = lockPatternPoint.getDistanceTo(nextLockPatternPoint)
//                var offsetX:Double = 0.toDouble()
//                var offsetY:Double = 0.toDouble()
//                if(lockPatternPoint.centerX == nextLockPatternPoint.centerX) {
//                    offsetY = abs(lockPatternPoint.centerY-nextLockPatternPoint.centerY)/(dx/innerRadius)
//                } else if(lockPatternPoint.centerY == nextLockPatternPoint.centerY) {
//                    offsetX = abs(lockPatternPoint.centerX-nextLockPatternPoint.centerX)/(dx/innerRadius)
//                }
//
//                //dx/radius = (dx/innerRadius/))/offset
//                //dx/radius = x/offset
//
//
//                canvas.drawLine(
//                    lockPatternPoint.centerX+offsetX.toFloat(),
//                    lockPatternPoint.centerY+offsetY.toFloat(),
//                    nextLockPatternPoint.centerX-offsetX.toFloat(),
//                    nextLockPatternPoint.centerY-offsetY.toFloat(),
//                    drawLinePaint
//                )
                //这里的5.0是什么意思？
//                val d = lockPatternPoint.getDistanceTo(nextLockPatternPoint)
//                val rx = (((nextLockPatternPoint.centerX - lockPatternPoint.centerX) * innerRadius).toDouble() / 5.0 / d).toFloat()
//                val ry = (((nextLockPatternPoint.centerY - lockPatternPoint.centerY) * innerRadius).toDouble() / 5.0 / d).toFloat()
//                canvas.drawLine(lockPatternPoint.centerX + rx, lockPatternPoint.centerY + ry, nextLockPatternPoint.centerX - rx, nextLockPatternPoint.centerY - ry, drawLinePaint)

            }
        }
    }


    fun drawArrowsBetweenPoints(canvas: Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {


        if (event?.action == MotionEvent.ACTION_MOVE || event?.action == MotionEvent.ACTION_DOWN) {
            //判断是否划到了一个新的点,是的话放到一个集合里
            val point = findLockPatternPoint(event.x, event.y)
            if (point != null && !selectedPoints.contains(point)) {
                point.state = LockPointState.Selected
                selectedPoints.add(point)
            }
        } else if (event?.action == MotionEvent.ACTION_UP) {
            //计算密码是否符合规则等。
            selectedPoints.clear()
            resetLockPatternPointsState()
        }
        lastMotionEvent = event
        invalidate()
        Log.d(TAG, "selectedPoints:$selectedPoints")
        return true
    }

    private fun findLockPatternPoint(x: Float, y: Float): LockPatternPoint? {
        return lockPatternPoints.firstOrNull {
            it.getDistanceTo(x, y) <= outerRadius
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width =
            MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd - paddingLeft - paddingRight
        val height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        val finalWidth = min(width, height)

        //当作为 ConstraintLayout的子View时，宽高会有一点奇怪的问题。有时宽高比setMeasuredDimension设置的要小。
        Log.d(
            TAG,
            "finalWidth:$finalWidth\tpaddingStart:$paddingStart\tpaddingEnd:$paddingEnd\tpaddingLeft:$paddingLeft\tpaddingRight:$paddingRight\tpaddingTop:$paddingTop\tpaddingBottom:$paddingBottom\twidthMeasureSpec:${
                MeasureSpec.toString(widthMeasureSpec)
            }\theightMeasureSpec:${MeasureSpec.toString(heightMeasureSpec)}"
        )
        setMeasuredDimension(finalWidth, finalWidth)
        initLockPatternPoints()
        outerRadius = measuredWidth / 3 / 2 * 0.618f
    }
}

class LockPatternPoint(
    val centerX: Float, val centerY: Float, val index: Int, var state: LockPointState
) {

    fun getDistanceTo(point: LockPatternPoint): Double {
        return getDistanceTo(point.centerX, point.centerX)
    }

    fun getDistanceTo(x: Float, y: Float): Double {
        val dx = abs(centerX - x)
        val dy = abs(centerY - y)

        val distance = sqrt(dx * dx.toDouble() + dy * dy.toDouble())
        return distance
    }
}

enum class LockPointState {
    Normal, Selected, Error
}