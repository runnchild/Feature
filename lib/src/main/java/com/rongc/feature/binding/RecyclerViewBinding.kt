package com.rongc.feature.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.viewmodel.RefreshEmptyViewModel
import com.rongc.feature.widget.EmptyView
import com.rongc.feature.widget.ItemDecoration
import java.lang.reflect.ParameterizedType

object RecyclerViewBinding {

    fun RecyclerView.setupEmptyView(): RefreshEmptyViewModel? {
        adapter ?: setup<Any>()
        val adapter = adapter as BaseQuickAdapter<*, *>
        var emptyViewModel: RefreshEmptyViewModel? = null
        if (!adapter.hasEmptyView()) {
            emptyViewModel = RefreshEmptyViewModel()
            adapter.setEmptyView(EmptyView(context).apply {
                setViewModel(emptyViewModel)
            })
        }
        return emptyViewModel
    }
}

@BindingAdapter("adapter")
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
@BindingAdapter("itemBinders")
fun <T> RecyclerView.itemBinders(binders: MutableList<out BaseRecyclerItemBinder<T>>) {
    if (binders.isNullOrEmpty()) {
        return
    }

    fun findBinderType(clazz: Class<*>?): ParameterizedType? {
        val type = clazz?.genericSuperclass ?: return null
        if (type is ParameterizedType) {
            return type
        }
        return findBinderType(clazz.superclass as? Class<*>)
    }

    val adapter = adapter as? BaseBinderAdapter ?: BaseBinderAdapter(null).apply { adapter = this }
    binders.forEach { item ->
        val arguments = findBinderType(item::class.java)!!.actualTypeArguments
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
    val adapter = adapter as? BaseBinderAdapter ?: BaseBinderAdapter(null).apply { adapter = this }
    adapter.setList(items)
}

@BindingAdapter("itemDecoration")
fun RecyclerView.itemDecoration(decorator: ItemDecoration) {
    addItemDecoration(decorator)
}