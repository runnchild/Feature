package com.rongc.feature.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.rongc.feature.R
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.ui.BinderAdapter
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
        val adapter = setup(adapter) as BaseBinderAdapter
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
        @Suppress("UNCHECKED_CAST")
        val adapter = setup(adapter) as BaseQuickAdapter<Any, *>
        adapter.setList(items)
    }

    @BindingAdapter("adapter")
    fun ViewPager2.setup(adapter: RecyclerView.Adapter<*>? = null): RecyclerView.Adapter<*> {
        // 是否使用DataBinding
//    val isBinding = getTag(R.id.layout_isBinding) as? Boolean == true
        if (adapter != null && this.adapter == adapter) {
            return adapter
        }
        val adapter1 = adapter ?: BinderAdapter(null)
        this.adapter = adapter1

        if (adapter1 is BaseQuickAdapter<*, *>) {
            @Suppress("UNCHECKED_CAST")
            val block = getTag(R.id.tag_adapter_callback) as? (BaseQuickAdapter<*, *>) -> Unit
            block?.invoke(adapter1)
            setTag(R.id.tag_adapter_callback, null)
        }
        return adapter1
    }

    fun <T : RecyclerView.Adapter<*>> ViewPager2.doOnAdapter(block: (T) -> Unit) {
        if (adapter != null) {
            @Suppress("UNCHECKED_CAST")
            block(adapter as T)
        } else {
            setTag(R.id.tag_adapter_callback, block)
        }
    }
}