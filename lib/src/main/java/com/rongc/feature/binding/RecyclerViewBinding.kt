package com.rongc.feature.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.ui.BinderAdapter
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
fun <T : Any> RecyclerView.setup(adapter: BaseQuickAdapter<T, BaseViewHolder>? = null) {
    // 是否使用DataBinding
//    val isBinding = getTag(R.id.layout_isBinding) as? Boolean == true
    if (adapter != null && this.adapter == adapter) {
        return
    }
    val adapter1 = adapter ?: BinderAdapter(null)
    this.adapter = adapter1
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

    val adapter = adapter as? BaseBinderAdapter ?: BinderAdapter(null).apply { adapter = this }
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
fun RecyclerView.items(items: Collection<Any>?) {
    val adapter = adapter as? BaseBinderAdapter ?: BinderAdapter(null).apply { adapter = this }
    adapter.setList(items)
}

@BindingAdapter("itemDecoration")
fun RecyclerView.itemDecoration(decorator: ItemDecoration?) {
    decorator?.let {
        removeItemDecoration(decorator)
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
    addItemDecoration(
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
    adapter ?: setup<Any>()
    val adapter = adapter as BaseQuickAdapter<*, *>
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