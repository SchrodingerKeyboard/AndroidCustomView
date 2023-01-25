package com.schrodinger.androidcustomview.customViews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.schrodinger.androidcustomview.R

class RatingView : View {

    private val TAG = "RatingViewTAG"
    private var normalDrawable: Bitmap? = null
    private var selectedDrawable: Bitmap? = null
    private var starCount: Int = 5
    private var starSize: Float = 0.0f
    //这里的间隔，其实不需要外面传进来，根据（总宽度-paddingStart-paddingEnd-所有星星的宽度）/（星星数-1）
    private var starInterval: Float = 5.0f

    private val paint = Paint().apply {
        isAntiAlias = true
    }

    private var selectedIndex: Int = -1

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

    private fun initAttrs(_context: Context, _attrs: AttributeSet) {
        val typeArray = _context.obtainStyledAttributes(_attrs, R.styleable.RatingView)
        starCount = typeArray.getInt(R.styleable.RatingView_starCount, 0)
        starSize = typeArray.getDimension(R.styleable.RatingView_starSize, 0.0f)
        starInterval = typeArray.getDimension(R.styleable.RatingView_starInterval, 0.0f)
        val normalDrawableResId = typeArray.getResourceId(R.styleable.RatingView_normalDrawable, 0)
        val selectedDrawableResId = typeArray.getResourceId(R.styleable.RatingView_selectedDrawable, 0)

        normalDrawable = getBitmapFromVectorDrawable(_context, normalDrawableResId)
        selectedDrawable = getBitmapFromVectorDrawable(_context, selectedDrawableResId)
        if (starSize == 0.0f) {
            starSize = normalDrawable?.width?.toFloat() ?: 0.0f
        }
        Log.d(TAG, "normalDrawable width:${normalDrawable?.width}\theight:${normalDrawable?.height}")
        Log.d(TAG, "selectedDrawable width:${selectedDrawable?.width}\theight:${selectedDrawable?.height}")

        val tag =
            "starCount:$starCount\tstarSize:$starSize\tstarInterval:$starInterval\tnormalDrawableResId:$normalDrawableResId\tselectedDrawableResId:$selectedDrawableResId"
        Log.d(TAG, tag)
        typeArray.recycle()
    }

    /**
     * SVG 转 Bitmap
     * @param context 上下文
     * @param drawableId SVG资源
     * @return
     */
    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (drawable == null) return null
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        val scaledBitmap = if (starSize != 0.0f && drawable.intrinsicWidth != starSize.toInt())
            Bitmap.createScaledBitmap(bitmap, starSize.toInt(), starSize.toInt(), false)
        else {
            bitmap
        }

        if (bitmap != scaledBitmap) {
            bitmap.recycle()
        }

        val canvas = Canvas(scaledBitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return scaledBitmap
    }

    override fun onDraw(canvas: Canvas?) {
        drawStar(canvas)
    }

    private fun drawStar(canvas: Canvas?) {
        val interval = getTotalInterval().toFloat() / (starCount - 1)
        Log.d(TAG, "interval/2:${interval / 2}")
        (0 until starCount).forEach { index ->
            val drawDrawable = if (index <= selectedIndex) selectedDrawable else normalDrawable
            drawDrawable?.let { drawable ->
                val startWidth = getPositionStart(index)
                canvas?.drawBitmap(drawable, startWidth, 0.0f, paint)
            }
        }
    }

    private fun getPositionStart(index: Int): Float {
        val allInterval = starInterval * index
        val startWidth = index * starSize * 1.0f + allInterval +paddingStart
        return startWidth
    }

    private fun getPosition(x: Float): Int {
        (0 until starCount).forEach {
            val start = getPositionStart(it)
            if (x in (start - starInterval / 2)..(start + starSize + starInterval / 2)) {
                return it
            }
        }
        return starCount-1
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    val x = event.x
                    val tempSelectedIndex = getPosition(x)
                    Log.d(
                        TAG,
                        "action:${it.action}\ttouch x:$x\ttempSelectedIndex:$tempSelectedIndex\tselectedIndex:$selectedIndex"
                    )
                    if (tempSelectedIndex != selectedIndex) {
                        selectedIndex = tempSelectedIndex
                        invalidate()
                    }
                }
                else -> {}
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure:${MeasureSpec.toString(widthMeasureSpec)}\t${MeasureSpec.toString(heightMeasureSpec)}")
        val height = if (starSize == 0.0f) normalDrawable?.height?.toFloat() ?: 10.0f else starSize
        val width = if (starSize == 0.0f) normalDrawable?.width?.toFloat() ?: 10.0f else starSize

        setMeasuredDimension((width * 5.0 + getTotalInterval()+paddingEnd+paddingStart).toInt(), height.toInt())
    }

    //总间隔数
    private fun getTotalInterval(): Int {
        return ((starCount - 1) * starInterval).toInt()
    }
}