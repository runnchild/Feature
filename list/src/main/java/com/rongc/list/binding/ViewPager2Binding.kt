package com.rongc.list.binding

import android.view.View
import androidx.core.view.doOnDetach
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rongc.feature.utils.removeFromParent
import com.rongc.list.ItemDecoration
import com.rongc.list.R
import com.rongc.list.ability.addDoOnAdapterCallback
import com.rongc.list.ability.invokeDoOnAdapter
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.adapter.BinderAdapter
import com.rongc.list.setCompatList
import com.rongc.list.viewpager2.BaseFragmentPagerAdapter
import com.runnchild.emptyview.EmptyView
import com.runnchild.emptyview.EmptyViewConfig
import com.runnchild.emptyview.IEmptyView
import kotlinx.coroutines.*

/**
 * 添加布局绑定Binder
 * 如果没有设置adapter默认会设置BaseBinderAdapter
 * @param binders  binders的大小代表不同布局的数量， 数据内容的类型不可一样
 */
@BindingAdapter("itemBinders")
fun ViewPager2.itemBinders(binders: List<BaseRecyclerItemBinder<out Any>>) {
    if (binders.isNullOrEmpty()) {
        return
    }
    doOnAdapter<RecyclerView.Adapter<*>> {
        binders.forEach { item ->
            itemBinder(item)
        }
    }
}

@BindingAdapter("itemBinder")
fun ViewPager2.itemBinder(binder: BaseRecyclerItemBinder<out Any>) {
    doOnAdapter<RecyclerView.Adapter<*>> {
        val actualClz = findBinderType(binder::class.java)!!.actualTypeArguments.last()

        @Suppress("UNCHECKED_CAST")
        val bin = binder as BaseRecyclerItemBinder<Any>
        (it as BaseBinderAdapter).addItemBinder(actualClz as Class<*>, bin, bin.callback)
    }
}

@BindingAdapter("itemBinderName")
fun ViewPager2.itemBinder(binderClz: String) {
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

/**
 * 绑定列表数据， 如果没设置adapter会默认设置BaseBinderAdapter
 */
@BindingAdapter("items")
fun ViewPager2.items(items: List<Any>?) {
    val adapter1 = adapter
    @Suppress("UNCHECKED_CAST")
    if (adapter1 as? BaseFragmentPagerAdapter<Any> != null) {
        adapter1.setList(items)
    } else {
        @Suppress("UNCHECKED_CAST")
        val adapter = setup(adapter) as? BaseBinderAdapter ?: return
        doOnAdapter<BaseBinderAdapter> {
            adapter.setCompatList(items)
        }
    }
}

@BindingAdapter("itemDecoration")
fun ViewPager2.itemDecoration(decorator: ItemDecoration?) {
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
fun ViewPager2.divider(
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

@BindingAdapter("adapter")
fun ViewPager2.setup(adapter: RecyclerView.Adapter<*>? = null): RecyclerView.Adapter<*> {
    if (adapter != null && this.adapter == adapter) {
        return adapter
    }
    val adapter1 = adapter ?: BinderAdapter(null)
    if (this.adapter == null) {
        this.adapter = adapter1
        invokeDoOnAdapter(this, this.adapter!!)
    }
    return adapter1
}

fun <T : RecyclerView.Adapter<*>> ViewPager2.doOnAdapter(block: (T) -> Unit) {
    if (adapter != null) {
        @Suppress("UNCHECKED_CAST")
        block(adapter as T)
    } else {
        addDoOnAdapterCallback(this, block)
    }
}

@BindingAdapter("auto_scroll", "scroll_interval", "loop", requireAll = false)
fun ViewPager2.loop(autoScroll: Boolean, interval: Long = 3000, loop: Boolean = false) {
    val jobTag = getTag(R.id.tag_job)
    if (jobTag == null) {
        doOnDetach {
            (getTag(R.id.tag_job) as? Job)?.cancel()
            val callbackTag = getTag(R.id.srl_tag)
            if (callbackTag is ViewPager2.OnPageChangeCallback) {
                unregisterOnPageChangeCallback(callbackTag)
            }
            setTag(R.id.tag_job, null)
        }
    }

    val callbackTag = getTag(R.id.srl_tag)
    if (autoScroll) {
        scroll(interval, loop)

        if (callbackTag !is ViewPager2.OnPageChangeCallback) {
            val value = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        (getTag(R.id.tag_job) as? Job)?.cancel()
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        scroll(interval, loop)
                    }
                }
            }

            setTag(R.id.srl_tag, value)
            registerOnPageChangeCallback(value)
        }
    } else {
        if (callbackTag is ViewPager2.OnPageChangeCallback) {
            unregisterOnPageChangeCallback(callbackTag)
            setTag(R.id.srl_tag, null)
        }
    }
}

private fun ViewPager2.scroll(interval: Long, loop: Boolean) {
    (getTag(R.id.tag_job) as? Job)?.cancel()
    val job = GlobalScope.launch(Dispatchers.Main) {
        while (true) {
            delay(interval)
            val count = adapter?.itemCount ?: -1
            if (currentItem in 0 until count) {
                if (currentItem + 1 == count) {
                    if (loop) {
                        currentItem = 0
                    }
                } else {
                    currentItem += 1
                }
            }
        }
    }
    setTag(R.id.tag_job, job)
}

@BindingAdapter("emptyView", "enableEmptyView", requireAll = false)
fun ViewPager2.setupEmptyView(
    emptyView: IEmptyView? = EmptyView(context), enable: Boolean = true
): EmptyViewConfig? {
    if (enable) {
        fun getEmptyView(): IEmptyView {
            val emptyViewModel = emptyView?.config ?: EmptyViewConfig()
            return (emptyView ?: EmptyView(context)).run {
                config = emptyViewModel
                this
            }
        }

        return adapter?.let {
            when (it) {
                is BaseQuickAdapter<*, *> -> {
                    val iEmpty = if (!it.hasEmptyView()) {
                        getEmptyView()
                    } else {
                        (it.headerLayout?.getChildAt(0) as? EmptyView)
                    }
                    it.setEmptyView(iEmpty as View)
                    iEmpty.removeFromParent()
                    iEmpty.config
                }
                is BaseFragmentPagerAdapter<*> -> {
                    val emptyView1 = getEmptyView()
                    it.setEmptyData(emptyView1.config!!)
                    emptyView1.config
                }
                else -> null
            }
        }
    }
    return null
}