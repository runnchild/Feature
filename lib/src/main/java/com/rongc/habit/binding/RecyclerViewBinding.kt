package com.rongc.habit.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.habit.refresh.BaseRecyclerItemBinder
import java.lang.reflect.ParameterizedType

fun <T : Any> RecyclerView.setup(adapter: BaseQuickAdapter<T, BaseViewHolder>? = null) {
    // 是否使用DataBinding
//    val isBinding = getTag(R.id.layout_isBinding) as? Boolean == true
    this.adapter = adapter ?: BaseBinderAdapter(null)
}

/**
 * 添加布局绑定Binder
 * 如果没有设置adapter默认会设置BaseBinderAdapter
 * @param binders  binders的大小代表不同布局的数量， 数据内容的类型不可一样
 */
//@BindingAdapter("itemBinders")
fun <T> RecyclerView.itemBinders(binders: MutableList<BaseRecyclerItemBinder<T>>) {
    if (binders.isNullOrEmpty()) {
        return
    }
    val adapter = adapter as? BaseBinderAdapter
    if (adapter == null) {
        setup<Any>()
    }
    binders.forEach { item ->
        val arguments =
            (item::class.java.genericSuperclass as ParameterizedType).actualTypeArguments
        val actualClz = arguments.lastOrNull() ?: return@forEach
        val method = BaseBinderAdapter::class.java.getDeclaredMethod(
            "addItemBinder",
            Class::class.java,
            BaseItemBinder::class.java,
            DiffUtil.ItemCallback::class.java
        )
        method.isAccessible = true
        method.invoke(adapter, actualClz, item, item.callback)
    }
}

/**
 * 绑定列表数据， 如果没设置adapter会默认设置BaseBinderAdapter
 */
@BindingAdapter("items")
fun RecyclerView.items(items: Collection<Any>) {
    val adapter = adapter as? BaseBinderAdapter
    if (adapter == null) {
        setup<Any>()
    }
    adapter?.setList(items)
}