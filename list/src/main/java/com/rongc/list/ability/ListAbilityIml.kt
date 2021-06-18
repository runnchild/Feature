package com.rongc.list.ability

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.Status
import com.rongc.list.ItemDecoration
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.adapter.BinderAdapter
import com.rongc.list.setCompatDiffNewData
import com.rongc.list.viewmodel.BaseListViewModel
import com.rongc.list.viewmodel.DefaultEmptyConfig
import com.rongc.list.viewmodel.EmptyBuilder
import com.rongc.list.viewmodel.RefreshEmptyViewModel
import com.rongc.list.widget.IEmptyView
import java.util.*

abstract class ListAbilityIml(val viewModel: BaseViewModel, private val listHost: IList) :
    IAbility {

    val adapter: RecyclerView.Adapter<*> by lazy {
        listHost.providerAdapter() ?: BinderAdapter()
    }

    private lateinit var emptyViewModel: RefreshEmptyViewModel

    override fun onCreate(owner: LifecycleOwner) {
        emptyViewModel = RefreshEmptyViewModel()
        setEmptyView(emptyViewModel)
        observeListResource(owner)

        val decoration = ItemDecoration.Builder().apply(listHost.decorationBuilder()).build()
        setupItemDecoration(decoration)

        val itemBinders = arrayListOf<BaseRecyclerItemBinder<out Any>>()
        listHost.registerItemBinders(itemBinders)
        setupItemBinders(itemBinders)
    }

    private fun observeListResource(owner: LifecycleOwner) {
        @Suppress("UNCHECKED_CAST")
        val vm = viewModel as? BaseListViewModel<Any>
        // 如果非BaseListViewModel则需要手动调用observeResource
        if (vm != null) {
            vm._autoRefresh = listHost.autoRefresh()
            owner.observeResource(adapter, vm)

            vm.setupEmptyView.observe(owner) { state ->
                val defaultBuilder = when (state) {
                    RefreshEmptyViewModel.State.EMPTY_NET_DISCONNECT -> DefaultEmptyConfig.noNetBuilder
                    RefreshEmptyViewModel.State.EMPTY_NET_UNAVAILABLE -> DefaultEmptyConfig.netUnavailableBuilder
                    else -> DefaultEmptyConfig.emptyDataBuilder
                }
                // 替换默认配置
                val emptyBuilder = EmptyBuilder().apply(defaultBuilder)
                    .apply(listHost.setupEmptyView(state))
                emptyBuilder.btnClick = { vm.refresh() }
                emptyViewModel.builder(emptyBuilder)
            }
        }
    }

    open fun setEmptyView(emptyViewModel: RefreshEmptyViewModel) {
        val providerAdapter = adapter
        if (providerAdapter is BaseQuickAdapter<*, *>) {
            val emptyView = providerEmptyView()
            emptyView.setViewModel(emptyViewModel)
            providerAdapter.setEmptyView(emptyView as View)
            // providerAdapter.headerWithEmptyEnable = true
            // providerAdapter.footerWithEmptyEnable = true
        }
    }

    abstract fun providerEmptyView(): IEmptyView

    abstract fun setupItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>)

    abstract fun setupItemDecoration(decoration: ItemDecoration)
}

/**
 * 订阅列表数据结果返回监听，当结果返回时刷新列表
 * 订阅前应先注册{@link ListAbility}，如果ViewModel继承的是BaseListViewModel,则在注册后会自动订阅。
 * 否则需手动调用
 */
@Suppress("UNCHECKED_CAST")
fun <T> LifecycleOwner.observeResource(
    adapter: RecyclerView.Adapter<*>, viewModel: BaseListViewModel<T>
) {
    viewModel.result.observe(this) { resource ->
        if (resource.status != Status.LOADING || resource.data != null) {
            if (adapter is BaseQuickAdapter<*, *>) {
                adapter as BaseQuickAdapter<T, BaseViewHolder>
                if (viewModel.isRefresh) {
                    adapter.setCompatDiffNewData(resource.data)
                } else {
                    resource.data?.let {
                        adapter.addData(it)
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> IHost<*>.observeResource(result: LiveData<Resource<List<T>>>) {
    findAbility { it is ListAbility }?.let {
        it as ListAbility
        result.observe(lifecycleOwner) { resource ->
            if (resource.status != Status.LOADING || resource.data != null) {
                val adapter = it.adapter
                if (adapter is BaseQuickAdapter<*, *>) {
                    adapter as BaseQuickAdapter<T, BaseViewHolder>
                    adapter.setDiffNewData(resource.data?.toMutableList())
                } else if (adapter is ListAdapter<*, *>) {
                    adapter as ListAdapter<T, *>
                    adapter.submitList(resource.data)
                }
            }
        }
    }
}
