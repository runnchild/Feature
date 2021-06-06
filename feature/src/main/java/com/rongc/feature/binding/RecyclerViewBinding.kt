package com.rongc.feature.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rongc.feature.R
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.refresh.BinderAdapter
import com.rongc.feature.refresh.ItemDecoration
import java.lang.reflect.ParameterizedType

/**
 * 添加布局绑定Binder
 * 如果没有设置adapter默认会设置BaseBinderAdapter
 * @param binders  binders的大小代表不同布局的数量， 数据内容的类型不可一样
 */
@BindingAdapter("itemBinders")
fun RecyclerView.itemBinders(binders: List<BaseRecyclerItemBinder<out Any>>) {
    if (binders.isNullOrEmpty()) {
        return
    }
    binders.forEach { item ->
        itemBinder(item)
    }
}

@BindingAdapter("itemBinderName")
fun RecyclerView.itemBinder(binderClz: String) {
    try {
        val instance = Class.forName(binderClz).newInstance()
        if (instance is BaseRecyclerItemBinder<*>) {
            @Suppress("UNCHECKED_CAST")
            itemBinder(instance as BaseRecyclerItemBinder<Any>)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("itemBinder")
fun RecyclerView.itemBinder(binder: BaseRecyclerItemBinder<out Any>) {
    val actualClz = findBinderType(binder::class.java)!!.actualTypeArguments.last()
    if (adapter == null) {
        adapter = BinderAdapter()
    }
    val adapter = adapter as BaseBinderAdapter
    @Suppress("UNCHECKED_CAST")
    val bin = binder as BaseRecyclerItemBinder<Any>
    adapter.addItemBinder(actualClz as Class<*>, bin, bin.callback)
}

fun findBinderType(clazz: Class<*>?): ParameterizedType? {
    val type = clazz?.genericSuperclass ?: return null
    if (type is ParameterizedType) {
        return type
    }
    return findBinderType(clazz.superclass as? Class<*>)
}

@BindingAdapter("itemDecoration")
fun RecyclerView.itemDecoration(decorator: ItemDecoration?) {
    decorator?.let {
        while (this.itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
        addItemDecoration(decorator)
    }
}

@BindingAdapter(
    "decoration_left",
    "decoration_top",
    "decoration_right",
    "decoration_bottom",
    "decoration_vertical_line",
    "decoration_horizontal_line",
    requireAll = false
)
fun RecyclerView.divider(
    left: Float = 0f,
    top: Float = 0f,
    right: Float = 0f,
    bottom: Float = 0f,
    vLine: Float = 0f,
    hLine: Float = 0f
) {
    itemDecoration(
        ItemDecoration.Builder()
            .setVerticalTopWidth(top.toInt())
            .setHorizontalStartWidth(left.toInt())
            .setHorizontalEndWidth(right.toInt())
            .setVerticalBottomWidth(bottom.toInt())
            .setVerticalLineWidth(vLine.toInt())
            .setHorizontalLineWidth(hLine.toInt())
            .build()
    )
}

/**
 * 如果此时还未设置adapter，将在设置了adapter后立即回调adapter。
 * 如果此时设置了adapter，则立即回调
 */
fun <T : RecyclerView.Adapter<*>> RecyclerView.doOnAdapter(block: (T) -> Unit) {
    if (adapter != null) {
        @Suppress("UNCHECKED_CAST")
        block(adapter as T)
    } else {
        setTag(R.id.tag_adapter_callback, block)
    }
}

/**
 * 如果此时还未设置adapter，将在设置了adapter后立即回调BaseQuickAdapter。
 * 如果此时设置了adapter，则立即回调BaseQuickAdapter
 */
fun <T> RecyclerView.doOnDefaultAdapter(block: (BaseQuickAdapter<T, *>) -> Unit) {
    return doOnAdapter(block)
}