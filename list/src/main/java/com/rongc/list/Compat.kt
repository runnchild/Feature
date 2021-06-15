package com.rongc.list

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

fun <T> BaseQuickAdapter<T, BaseViewHolder>.setCompatDiffNewData(data: List<T>?) {
    try {
        setDiffNewData(data?.toMutableList())
    } catch (e: NoSuchMethodError) {
        invokeForLower(this, data)
    }
}

private fun <T> invokeForLower(adapter: RecyclerView.Adapter<*>, data: List<T>?) {
    val method = adapter::class.java.getMethod("setDiffNewData", List::class.java)
    method.isAccessible = true
    method.invoke(adapter, data)
}

private fun <T> List<T>.toMutableList(): ArrayList<T> {
    return if (this is ArrayList<*>) {
        this as ArrayList<T>
    } else {
        ArrayList(this)
    }
}

fun <T> BaseQuickAdapter<T, *>.setCompatList(items: List<T>?) {
    try {
        setList(items)
    } catch (e: NoSuchMethodError) {
        // for lower version adapter
        val method = this::class.java.getMethod("setList", List::class.java)
        method.isAccessible = true
        method.invoke(this, items)
    }
}