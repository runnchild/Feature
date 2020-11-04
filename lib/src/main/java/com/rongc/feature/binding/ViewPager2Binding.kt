package com.rongc.feature.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import java.lang.reflect.ParameterizedType
object ViewPager2Binding {
    /**
     * 添加布局绑定Binder
     * 如果没有设置adapter默认会设置BaseBinderAdapter
     * @param binders  binders的大小代表不同布局的数量， 数据内容的类型不可一样
     */
    @JvmStatic
    @BindingAdapter("itemBinders")
    fun <T> ViewPager2.itemBinders(binders: MutableList<out BaseRecyclerItemBinder<T>>) {
        if (binders.isNullOrEmpty()) {
            return
        }
        val adapter = adapter as? BaseBinderAdapter ?: BaseBinderAdapter().apply { adapter = this }
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
    @JvmStatic
    @BindingAdapter("items")
    fun ViewPager2.items(items: Collection<Any>) {
        val adapter = adapter as? BaseBinderAdapter ?: BaseBinderAdapter().apply { adapter = this }
        adapter.setList(items)
    }
}