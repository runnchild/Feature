package com.rongc.feature.ui.delegate

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rongc.feature.binding.ViewPager2Binding.doOnAdapter
import com.rongc.feature.binding.ViewPager2Binding.itemBinders
import com.rongc.feature.binding.ViewPager2Binding.itemDecoration
import com.rongc.feature.model.BaseModel
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.widget.ItemDecoration
import java.util.*

abstract class ViewPagerAbility(private val viewModel: BaseListViewModel<*, out BaseModel>) : IFragmentAbility {

    lateinit var mAdapter: RecyclerView.Adapter<*>
    abstract val baseViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.autoRefresh = autoRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        baseViewPager.run {
            adapter = obtainAdapter()
            itemDecoration(itemDecoration)
            doOnAdapter<RecyclerView.Adapter<*>> {
                mAdapter = it
                if (it is BaseQuickAdapter<*, *>) {
                    val binders = arrayListOf<BaseRecyclerItemBinder<out Any>>()
                    obtainItemBinders(binders)
                    if (binders.isNotEmpty()) {
                        @Suppress("UNCHECKED_CAST")
                        itemBinders(binders as ArrayList<BaseRecyclerItemBinder<Any>>)
                    }
                }
            }
        }
    }

    open fun obtainItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
    }

    /**
     * 默认使用BaseBinderAdapter，如果item需要使用Fragment，返回FragmentStateAdapter
     */
    open fun obtainAdapter(): RecyclerView.Adapter<*>? = null

    open fun autoRefresh() = true

    val itemDecoration: ItemDecoration
        get() = ItemDecoration.Builder().apply(decorationBuilder()).build()

    open fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return { }
    }

    open fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit = {}
}