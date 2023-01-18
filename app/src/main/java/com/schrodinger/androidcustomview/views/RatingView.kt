package com.schrodinger.androidcustomview.views

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
    private var starSize: Float = 5.0f
    private var starInterval: Float = 5.0f

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private val starsRanges = mutableListOf<ClosedRange<Float>>()
    private var selectedIndex:Int = -1

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
                val typeArray = _context.obtainStyledAttributes(_attrs, R.styleable.RatingView)
                starCount = typeArray.getInt(R.styleable.RatingView_starCount, 0)
                starSize = typeArray.getDimension(R.styleable.RatingView_starSize, 0.0f)
                starInterval = typeArray.getDimension(R.styleable.RatingView_starInterval, 0.0f)
                val normalDrawableResId = typeArray.getResourceId(R.styleable.RatingView_normalDrawable, 0)
                val selectedDrawableResId = typeArray.getResourceId(R.styleable.RatingView_selectedDrawable, 0)

                normalDrawable = getBitmapFromVectorDrawable(_context, normalDrawableResId)
                selectedDrawable = getBitmapFromVectorDrawable(_context, selectedDrawableResId)

                Log.d(TAG, "normalDrawable width:${normalDrawable?.width}\theight:${normalDrawable?.height}")
                Log.d(TAG, "selectedDrawable width:${selectedDrawable?.width}\theight:${selectedDrawable?.height}")

                val tag =
                    "starCount:$starCount\tstarSize:$starSize\tstarInterval:$starInterval\tnormalDrawableResId:$normalDrawableResId\tselectedDrawableResId:$selectedDrawableResId"
                Log.d(TAG, tag)
                typeArray.recycle()
            }
        }
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

        val scaledBitmap = if (drawable.intrinsicWidth != starSize.toInt())
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
        starsRanges.clear()
        (0..starCount).forEach { index ->
            val drawDrawable = if (index <= selectedIndex) selectedDrawable else normalDrawable
            drawDrawable?.let { drawable ->
                val endInterval = interval * index
                val startWidth = index * drawable.width * 1.0f + endInterval

                val indexStartWidth = startWidth - interval / 2
                starsRanges.add((if (indexStartWidth < 0) 0.0f else indexStartWidth)..(startWidth + drawable.width + (interval / 2)))
                canvas?.drawBitmap(drawable, startWidth, 0.0f, paint)
            }
        }
        Log.d(TAG, "starsRanges:$starsRanges")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    val x = event.x
                    val tempSelectedIndex = starsRanges.indexOfFirst {
                        x in it
                    }
                    Log.d(TAG, "action:${it.action}\ttouch x:$x\ttempSelectedIndex:$tempSelectedIndex\tselectedIndex:$selectedIndex")
                    if(tempSelectedIndex != selectedIndex) {
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure:${MeasureSpec.toString(widthMeasureSpec)}\t${MeasureSpec.toString(heightMeasureSpec)}")
        val height = if (starSize == 0.0f) normalDrawable?.height ?: 10 else starSize
        val width = if (starSize == 0.0f) normalDrawable?.width ?: 10 else starSize

        setMeasuredDimension(width.toInt() * 5 + getTotalInterval(), height.toInt())
    }

    //总间隔数
    private fun getTotalInterval(): Int {
        return ((starCount - 1) * starInterval).toInt()
    }
}