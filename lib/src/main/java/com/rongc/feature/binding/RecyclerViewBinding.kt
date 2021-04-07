package com.rongc.feature.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.rongc.feature.R
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.ui.adapter.BinderAdapter
import com.rongc.feature.utils.Compat.removeFromParent
import com.rongc.feature.viewmodel.RefreshEmptyViewModel
import com.rongc.feature.widget.EmptyView
import com.rongc.feature.widget.IEmptyView
import com.rongc.feature.widget.ItemDecoration
import java.lang.reflect.ParameterizedType

object RecyclerViewBinding {

    fun RecyclerView.getEmptyViewModel(): RefreshEmptyViewModel? {
        val adapter = adapter as BaseQuickAdapter<*, *>
        return if (adapter.emptyLayout?.childCount != 0) {
            val emptyView = adapter.emptyLayout?.getChildAt(0) as? EmptyView
            emptyView?.getViewModel()
        } else null
    }
}

@BindingAdapter("adapter")
fun RecyclerView.setup(adapter: RecyclerView.Adapter<*>? = null): RecyclerView.Adapter<*> {
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

/**
 * 添加布局绑定Binder
 * 如果没有设置adapter默认会设置BaseBinderAdapter
 * @param binders  binders的大小代表不同布局的数量， 数据内容的类型不可一样
 */
@BindingAdapter("itemBinders")
fun <T> RecyclerView.itemBinders(binders: MutableList<out BaseRecyclerItemBinder<T>>?) {
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

    val adapter = setup(adapter)
    binders.forEach { item ->
        val arguments = findBinderType(item::class.java)!!.actualTypeArguments
        var actualClz = arguments.lastOrNull() ?: return@forEach

        while (actualClz is ParameterizedType) {
            actualClz = actualClz.rawType
        }

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

@BindingAdapter("itemBinderName")
fun <T> RecyclerView.itemBinder(binderClz: String) {
    try {
        val instance = Class.forName(binderClz).newInstance()
        if (instance is BaseRecyclerItemBinder<*>) {
            itemBinder(instance)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("itemBinder")
fun <T> RecyclerView.itemBinder(binder: BaseRecyclerItemBinder<T>) {
    itemBinders(arrayListOf(binder))
}

/**
 * 绑定列表数据， 如果没设置adapter会默认设置BaseBinderAdapter
 */
@BindingAdapter("items")
fun RecyclerView.items(items: Collection<Any>?) {
    val adapter = setup(adapter) as BaseBinderAdapter
    adapter.setList(items)
//    if (items is ObservableArrayList<Any>) {
//        adapter.setList(items)
//        return
//    }
//    val data = if (items is MutableList<Any>) {
//        items
//    } else {
//        items?.toMutableList()
//    }
//    adapter.setDiffNewData(data)
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

@BindingAdapter("emptyView", "enableEmptyView", requireAll = false)
fun RecyclerView.setupEmptyView(
    emptyView: IEmptyView? = EmptyView(context),
    enable: Boolean = true
): RefreshEmptyViewModel? {
    val adapter = setup(adapter) as BaseQuickAdapter<*, *>
    return if (enable && !adapter.hasEmptyView()) {
        val emptyViewModel = emptyView?.getViewModel() ?: RefreshEmptyViewModel()
        val emptyView1 = (emptyView ?: EmptyView(context)).run {
            setViewModel(emptyViewModel)
            this as View
        }
        emptyView1.removeFromParent()
        adapter.setEmptyView(emptyView1)
        emptyViewModel
    } else {
        (adapter.headerLayout?.getChildAt(0) as? EmptyView)?.getViewModel()
    }
}

fun RecyclerView.baseAdapter() = adapter as? BaseQuickAdapter<*, *>

fun RecyclerView.doOnAdapter(block: (BaseQuickAdapter<Any, *>) -> Unit) {
    if (adapter != null) {
        @Suppress("UNCHECKED_CAST")
        block(adapter as BaseQuickAdapter<Any, *>)
    } else {
        setTag(R.id.tag_adapter_callback, block)
    }
}