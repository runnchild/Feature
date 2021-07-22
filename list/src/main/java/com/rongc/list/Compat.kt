package com.rongc.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.GridSpanSizeLookup
import com.chad.library.adapter.base.viewholder.BaseViewHolder

fun <T> BaseQuickAdapter<T, BaseViewHolder>.setCompatDiffNewData(data: List<T>?) {
    try {
        setDiffNewData(data?.toMutableList())
    } catch (e: NoSuchMethodError) {
        invokeForLower("setDiffNewData", data, List::class.java)
    }
}

private fun <T> BaseQuickAdapter<T, *>.invokeForLower(
    methodName: String, obj: Any?, type: Class<*>
) {
    // for lower version adapter
    try {
        val method = this::class.java.getMethod(methodName, type)
        method.isAccessible = true
        method.invoke(this, obj)
    } catch (e: Exception) {
    }
}

//private fun <T> List<T>.toMutableList(): ArrayList<T> {
//    return if (this is ArrayList<*>) {
//        this as ArrayList<T>
//    } else {
//        ArrayList(this)
//    }
//}

fun <T> BaseQuickAdapter<T, *>.setCompatList(items: List<T>?) {
    try {
        setList(items)
    } catch (e: NoSuchMethodError) {
        invokeForLower("setList", items, List::class.java)
    }
}

fun <T> BaseQuickAdapter<T, *>.setCompatGridSpanSizeLookup(lookup: GridSpanSizeLookup) {
    try {
        setGridSpanSizeLookup(lookup)
    } catch (e: NoSuchMethodError) {
        invokeForLower("setGridSpanSizeLookup", lookup, GridSpanSizeLookup::class.java)
    }
}