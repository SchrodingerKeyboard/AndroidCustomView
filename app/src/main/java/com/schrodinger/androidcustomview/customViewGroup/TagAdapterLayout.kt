package com.schrodinger.androidcustomview.customViewGroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener

class TagAdapterLayout : BaseTagLayout {


    override var TAG: String = "TagAdapterLayoutTAG"
    private var adapter: TagLayoutAdapter<out Any>? = null
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    private fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged count:${adapter?.getCount()}")
        //removeAllViews =   removeAllViewsInLayout(); requestLayout(); invalidate(true);
//        removeAllViews()
        removeAllViewsInLayout()
        //addView 也会执行 requestLayout();  invalidate(true);
        adapter?.datas?.forEachIndexed { index, any ->
            adapter?.getView(index, this@TagAdapterLayout)?.let {itemView->
                addView(itemView)
                itemView.setOnClickListener {
                    adapter?.onItemClickListener?.onItemClick(null,itemView,index,index.toLong())
                }
                //这个长按事件可以写在外面
                itemView.setOnLongClickListener {
                    adapter?.remove(index)
                    onDataSetChanged()
                    true
                }
            }

        }
        //因为addView内部已经调用了requestLayout，这里就先不用写了
//        requestLayout()
    }

    fun setAdapter(adapter: TagLayoutAdapter<out Any>) {
        adapter.unregisterDataSetObserver(::onDataSetChanged)
        adapter.registerDataSetObserver(::onDataSetChanged)
        this.adapter = adapter
        onDataSetChanged()
    }
}

typealias TagAdapterDataSetChangedListener = () -> Unit

abstract class TagLayoutAdapter<T>(val datas: MutableList<T>,val onItemClickListener: OnItemClickListener) {

    private val onDataSetChangedListeners = mutableListOf<() -> Unit>()

    fun clearData() {
        datas.clear()
    }

    fun add(data: T) {
        datas.add(data)
    }

    fun remove(data: T) = datas.remove(data)
    fun remove(index: Int) = datas.removeAt(index)

    fun notifyDataSetChanged() {
        onDataSetChangedListeners.forEach {
            it.invoke()
        }
    }

    fun registerDataSetObserver(listener: TagAdapterDataSetChangedListener) {
        if (!onDataSetChangedListeners.contains(listener)) {
            onDataSetChangedListeners.add(listener)
        }
    }

    fun unregisterDataSetObserver(listener: TagAdapterDataSetChangedListener) {
        onDataSetChangedListeners.remove(listener)
    }

    fun getCount(): Int = datas.size

    fun getItem(position: Int): T = datas[position]
    abstract fun getView(position: Int, parent: ViewGroup): View

}
