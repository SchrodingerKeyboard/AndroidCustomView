package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView

//HorizontalScrollView是怎么实现滑动的 overScroll
class CustomSlidingMenu : HorizontalScrollView {

    private val TAG = "CustomSlidingMenuTAG"
    private var screenWidth:Int =  resources.displayMetrics.widthPixels
    private var screenHeight:Int =  resources.displayMetrics.heightPixels
    private var menuView: View? = null
    private var contentView:View? = null
    private var shadowView:View? = null
    private var menuIsOpen:Boolean = false
    private var menuWidth:Int = 0
    private var gestureDetector:GestureDetector? = null
    private var isInterceptTouch = false

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        //加上这个，体验会好很多，就是在快速滑动(按下，快速滑动，抬起)，但是滑动距离很短时，可以看出区别
        gestureDetector= GestureDetector(context,object : SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                //当手指快速滑动时回调的方法
                Log.d(TAG,"onFling velocityX:$velocityX\tvelocityY:$velocityY\te1:$e1\te2:$e2")
                if(menuIsOpen) {
                    if(velocityX <- 500) {
                        toggleMenu()
                        return true
                    }
                } else {
                    if(velocityX > 500) {
                        toggleMenu()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun openMenu() {
        Log.d(TAG,"openMenu")
        smoothScrollTo(0,0)
        menuIsOpen = true
    }

    private fun closeMenu() {
        Log.d(TAG,"closeMenu")
        smoothScrollTo(menuWidth,0)
        menuIsOpen = false
    }

    private fun toggleMenu() {
        Log.d(TAG,"toggleMenu")
        if(menuIsOpen) closeMenu() else openMenu()
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        Log.d(TAG,"onOverScrolled scrollX:$scrollX\tscrollY:$scrollY\tclampedX:$clampedX\tclampedY:$clampedY")
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    override fun scrollTo(x: Int, y: Int) {
        Log.d(TAG,"scrollTo:x:$x\ty:$y")
        super.scrollTo(x, y)
    }

    override fun scrollBy(x: Int, y: Int) {
        Log.d(TAG,"scrollBy:x:$x\ty:$y")
        super.scrollBy(x, y)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"onInterceptTouchEvent:ev:$ev")
        if(menuIsOpen) {
            val currentX = ev?.x?:0f
            if(currentX > menuWidth) {
                //关闭菜单
                closeMenu()
                //子View不需要响应任何事件（点击和触摸）拦截子View的事件,虽然子View不处理，但本身的onTouch又会执行，close了，但是又会open
                isInterceptTouch = true
                return true
            }

        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"onTouchEvent:ev:$ev")
        if(isInterceptTouch) {
            isInterceptTouch = false
            return true
        }
        //处理手指快速滑动,这里的返回值，跟gestureDetector里的listener重写的返回值有关,可以看看里面的源码是怎么实现的。
        if(gestureDetector?.onTouchEvent(ev) == true) {
            return gestureDetector?.onTouchEvent(ev)?:false
        }

        when(ev?.action) {
            //当手指抬起时，判断一下大概在屏幕（menuView）的哪一个位置，靠左就关闭菜单，靠右就打开菜单
            MotionEvent.ACTION_UP -> {
                val currentScrollX = scrollX
                Log.d(TAG,"scrollX:$scrollX")
                if(currentScrollX > menuWidth/2) {
                    closeMenu()
                } else {
                    openMenu()
                }
                return false
            }
        }
        return super.onTouchEvent(ev)
    }

    //需要在onMeasure前给子View设置宽高，onFinishInflate是一个比较合适的方法
    override fun onFinishInflate() {
        Log.d(TAG,"onFinishInflate")
        super.onFinishInflate()
        val container = getChildAt(0) as? ViewGroup ?: return

        menuView = container.getChildAt(0)
        val contentView = container.getChildAt(1)

        menuWidth = screenWidth - 100
        menuView?.layoutParams?.width = menuWidth
        contentView.layoutParams?.width = screenWidth

        //这里需要考虑container的类型，不是所有ViewGroup都是直接addView,removeView就可以的，
        // LinerLayout比较简单，但是ConstraintLayout会复杂一些
        container.removeView(contentView)
        val shadowContentView = FrameLayout(context)

        container.addView(shadowContentView,ViewGroup.LayoutParams(screenWidth,ViewGroup.LayoutParams.MATCH_PARENT))

        val imageView = ImageView(context).apply {
            setBackgroundColor(Color.parseColor("#99000000"))
            alpha = 0.0f
        }


        shadowContentView.addView(contentView,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
        shadowContentView.addView(imageView,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))

        this@CustomSlidingMenu.contentView  = shadowContentView
        shadowView = imageView
        if(container.childCount != 2) {
            throw  java.lang.IllegalStateException("只能有两个View，一个是menuView,contentView")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG,"onMeasure:ev widthMeasureSpec:${MeasureSpec.toString(widthMeasureSpec)}\theightMeasureSpec:${MeasureSpec.toString(heightMeasureSpec)}")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG,"onLayout changed:$changed\tl:$l\tt:$t\tr:$r\tb:$b")
        super.onLayout(changed, l, t, r, b)
        if(changed) {
            closeMenu()
        }
//        GestureDetector.OnGestureListener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG,"onSizeChanged w:$w\th:$h\toldw:$w\toldh:$oldh")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    //实现菜单左边抽屉样式的动画效果,滑动差效果
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        Log.d(TAG,"onScrollChanged l:$l\tt:$t\toldl:$oldl\toldt:$oldt")
        super.onScrollChanged(l, t, oldl, oldt)

        val scale = l*1f/menuWidth
        menuView?.translationX = l * 0.8f
        shadowView?.alpha = 1 - scale
        Log.d(TAG,"onScrollChanged l:$l\tt:$t\toldl:$oldl\toldt:$oldt\tshadowView:$shadowView\talpha:$alpha")

        val rightScale = 0.7f + 0.3f * scale
        contentView?.run {
            pivotX = 0f
            pivotY = measuredHeight /2.0f
            scaleX = rightScale
            scaleY = rightScale
        }

    }
}