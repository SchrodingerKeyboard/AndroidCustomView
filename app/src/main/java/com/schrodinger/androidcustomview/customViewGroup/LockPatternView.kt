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
import kotlin.math.roundToInt
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

    private var innerRadius = 15f
    private var outerRadius = 0f


    //画圆的画笔
    private val drawGesturePointPaint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        //设置画笔为空间
        style = Paint.Style.STROKE
        strokeWidth = 10f
        textSize = 28f
    }

    //画线的画笔
    private val drawLinePaint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        //设置画笔为空间
        style = Paint.Style.FILL
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND

    }

    //存九个点的数据集合
    private val lockPatternPoints = mutableListOf<LockPatternPoint>()

    //已经选择了的点的画笔
    private val selectedPoints = mutableListOf<LockPatternPoint>()

    //密码长度必须大于等于5
    private val passwordLength = 5

    private var lastTouchPoint: LockPatternPoint? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context, attrs, defStyleAttr, 0
    )

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {

    }

    private fun resetAllLockPatternPointsState(state: LockPointState) {
        lockPatternPoints.forEach {
            it.state = state
        }
    }

    private fun changeToErrorState() {
        lastTouchPoint = null
        selectedPoints.forEach {
            it.state = LockPointState.Error
        }
    }

    private fun initLockPatternPoints() {
        //初始化九个点的信息
        lockPatternPoints.clear()
        var index = 0
        val rowWidth = measuredWidth.toFloat() / 3
        (0..2).forEach { row ->
            val cy = (row * rowWidth) + rowWidth / 2
            (0..2).forEach { column ->
                val cx = column * rowWidth + rowWidth / 2
                lockPatternPoints.add(
                    LockPatternPoint(
                        centerX = cx.roundToInt().toFloat(),
                        centerY = cy.roundToInt().toFloat(),
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
            drawGesturePointPaint.color = getPaintColor(state = it.state)

            canvas.drawCircle(it.centerX.toFloat(), it.centerY.toFloat(), innerRadius.toFloat(), drawGesturePointPaint)

            //画外圆大圆心
            drawGesturePointPaint.color = getPaintColor(state = it.state)

            canvas.drawCircle(it.centerX.toFloat(), it.centerY.toFloat(), outerRadius.toFloat(), drawGesturePointPaint)
//            canvas.drawText("${lockPatternPoints.indexOf(it)}",it.centerX.toFloat(),it.centerY.toFloat(),drawGesturePointPaint)
        }
    }

    private fun getPaintColor(state: LockPointState): Int {
        return if (state == LockPointState.Normal) innerNormalColor else if (state == LockPointState.Selected) innerPressedColor else innerErrorColor
    }

    fun drawLineBetweenPoints(canvas: Canvas) {
        drawLinePaint.color = getPaintColor(LockPointState.Selected)
        Log.d(TAG, "drawLineBetweenPoints size:${selectedPoints.size}")
        selectedPoints.forEachIndexed { index, lockPatternPoint ->
            val nextLockPatternPoint = if (index >= selectedPoints.size - 1) null else selectedPoints[index + 1]
            if (nextLockPatternPoint == null) {
                lastTouchPoint?.let {
                    drawLineBetweenTwoPoint(canvas, lockPatternPoint, it, true)
                }
            } else {
                drawLineBetweenTwoPoint(canvas, lockPatternPoint, nextLockPatternPoint)
            }
        }
    }

    private fun drawLineBetweenTwoPoint(
        canvas: Canvas, pointFirst: LockPatternPoint, pointSecondOrTouch: LockPatternPoint, isSecondPointOnTouch: Boolean = false
    ) {
        if (isSecondPointOnTouch && pointFirst.getDistanceTo(pointSecondOrTouch) < innerRadius) {
            return
        }
        //从上到下画，从左到右画，为了计算圆心到小圆的偏移
        val startPoint =
            if (pointFirst.centerX < pointSecondOrTouch.centerX || pointFirst.centerY < pointSecondOrTouch.centerY) pointFirst else pointSecondOrTouch
        val endPoint = if (startPoint === pointFirst) pointSecondOrTouch else pointFirst
        //要的效果是起点和终点不是在两个圆心上，而是在两个内圆边上，所以要求起点和终点x,y的偏移
        //内圆半径/直角斜边 = x的偏移量/x1-x2
        //内圆半径/直角斜边 = y的偏移量/y1-y2
        val offsetX =
            if (endPoint.centerX != startPoint.centerX) innerRadius / startPoint.getDistanceTo(endPoint) * (endPoint.centerX - startPoint.centerX) else 0f
        val offsetY =
            if (endPoint.centerY != startPoint.centerY) innerRadius / startPoint.getDistanceTo(endPoint) * (endPoint.centerY - startPoint.centerY) else 0f
        var finalStartX = startPoint.centerX + offsetX
        var finalStartY = startPoint.centerY + offsetY
        var finalEndX = endPoint.centerX - offsetX
        var finalEndY = endPoint.centerY - offsetY

        if (isSecondPointOnTouch && startPoint === pointSecondOrTouch) {
            finalStartX -= offsetX
            finalStartY -= offsetY
        }

        if (isSecondPointOnTouch && endPoint === pointSecondOrTouch) {
            finalEndX += offsetX
            finalEndY += offsetY
        }
        drawLinePaint.color = getPaintColor(if (startPoint == null) endPoint.state else startPoint.state)
        canvas.drawLine(finalStartX, finalStartY, finalEndX, finalEndY, drawLinePaint)
    }

    fun drawArrowsBetweenPoints(canvas: Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_MOVE || event?.action == MotionEvent.ACTION_DOWN) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                selectedPoints.clear()
                resetAllLockPatternPointsState(LockPointState.Normal)
            }

            //判断是否划到了一个新的点,是的话放到一个集合里
            val point = findLockPatternPoint(event.x, event.y)
            if (point != null && !selectedPoints.contains(point)) {
                point.state = LockPointState.Selected
                selectedPoints.add(point)
                Log.d(TAG, "lastMotionEvent:${event}")
            }
            lastTouchPoint = LockPatternPoint(
                centerX = event.getX(event.actionIndex), centerY = event.getY(event.actionIndex), -1, LockPointState.Normal
            )
        } else if (event?.action == MotionEvent.ACTION_UP) {
            //计算密码是否符合规则等。
            if (selectedPoints.size >= passwordLength) {
                //回调成功
                selectedPoints.clear()
                resetAllLockPatternPointsState(LockPointState.Normal)
            } else {
                //提示失败
                changeToErrorState()
            }

        }
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
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd - paddingLeft - paddingRight
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
        outerRadius = ((measuredWidth.toFloat() - 4) / 3 / 2 * 0.618).toFloat()
    }
}

class LockPatternPoint(
    val centerX: Float,
    val centerY: Float,
    val index: Int,
    var state: LockPointState,
) {

    fun getDistanceTo(point: LockPatternPoint): Float {
        return getDistanceTo(point.centerX, point.centerY)
    }

    fun getDistanceTo(x: Float, y: Float): Float {
        val dx = abs(centerX - x)
        val dy = abs(centerY - y)

        return sqrt(dx * dx + dy * dy)
    }
}

enum class LockPointState {
    Normal, Selected, Error
}