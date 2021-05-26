package com.rongc.feature.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rongc.feature.R
import com.rongc.feature.refresh.ItemDecoration

/**
 * 添加布局绑定Binder
 * 如果没有设置adapter默认会设置BaseBinderAdapter
 * @param binders  binders的大小代表不同布局的数量， 数据内容的类型不可一样
 */
//@BindingAdapter("itemBinders")
//fun <T> RecyclerView.itemBinders(binders: MutableList<out BaseRecyclerItemBinder<T>>?) {
//    if (binders.isNullOrEmpty()) {
//        return
//    }
//
//    fun findBinderType(clazz: Class<*>?): ParameterizedType? {
//        val type = clazz?.genericSuperclass ?: return null
//        if (type is ParameterizedType) {
//            return type
//        }
//        return findBinderType(clazz.superclass as? Class<*>)
//    }
//
//    val adapter = setup(adapter)
//    binders.forEach { item ->
//        val arguments = findBinderType(item::class.java)!!.actualTypeArguments
//        var actualClz = arguments.lastOrNull() ?: return@forEach
//
//        while (actualClz is ParameterizedType) {
//            actualClz = actualClz.rawType
//        }
//
//        val method = BaseBinderAdapter::class.java.getDeclaredMethod(
//            "addItemBinder",
//            Class::class.java,
//            BaseItemBinder::class.java,
//            DiffUtil.ItemCallback::class.java
//        )
//        method.isAccessible = true
//        method.invoke(adapter, actualClz, item, item.callback)
//    }
//}
//
//@BindingAdapter("itemBinderName")
//fun <T> RecyclerView.itemBinder(binderClz: String) {
//    try {
//        val instance = Class.forName(binderClz).newInstance()
//        if (instance is BaseRecyclerItemBinder<*>) {
//            itemBinder(instance)
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}
//
//@BindingAdapter("itemBinder")
//fun <T> RecyclerView.itemBinder(binder: BaseRecyclerItemBinder<T>) {
//    itemBinders(arrayListOf(binder))
//}

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

fun RecyclerView.baseAdapter() = adapter as? BaseQuickAdapter<*, *>


fun <T : RecyclerView.Adapter<*>> RecyclerView.doOnAdapter(block: (T) -> Unit) {
    if (adapter != null) {
        @Suppress("UNCHECKED_CAST")
        block(adapter as T)
    } else {
        setTag(R.id.tag_adapter_callback, block)
    }
}

fun <T> RecyclerView.doOnDefaultAdapter(block: (BaseQuickAdapter<T, *>) -> Unit) {
    return doOnAdapter(block)
}