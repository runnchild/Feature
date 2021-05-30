package com.rongc.feature.ability.impl

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rongc.feature.R
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ability.IListAbility
import com.rongc.feature.binding.itemBinders
import com.rongc.feature.binding.itemDecoration
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.refresh.BinderAdapter
import com.rongc.feature.refresh.ItemDecoration
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.viewmodel.DefaultEmptyConfig
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.viewmodel.RefreshEmptyViewModel
import com.rongc.feature.widget.EmptyView
import com.rongc.feature.widget.IEmptyView

class ListAbility(private val host: IHost<*>, private val listHost: IListAbility) : IAbility {

    lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var emptyView: IEmptyView

    override fun onCreate(owner: LifecycleOwner) {
        @Suppress("UNCHECKED_CAST")
        val vm = host.viewModel as? BaseListViewModel<Any>
        // 如果非BaseListViewModel则需要手动调用observeResource
        if (vm != null) {
            host.observeResource(vm.result)

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
                emptyView.getViewModel()?.builder(emptyBuilder)
            }

            vm._autoRefresh = listHost.autoLoad()
        }

        val recyclerView = listHost.recyclerView
        recyclerView.layoutManager = listHost.providerLayoutManager(recyclerView.context)

        val providerAdapter = listHost.providerAdapter() ?: BinderAdapter()
        adapter = providerAdapter
        recyclerView.adapter = providerAdapter
        @Suppress("UNCHECKED_CAST")
        val call = recyclerView.getTag(R.id.tag_adapter_callback)
                as? (RecyclerView.Adapter<*>) -> Unit
        call?.invoke(adapter)

        val decoration = ItemDecoration.Builder().apply(listHost.decorationBuilder()).build()
        recyclerView.itemDecoration(decoration)

        val itemBinders = arrayListOf<BaseRecyclerItemBinder<out Any>>()
        listHost.registerItemBinders(itemBinders)
        recyclerView.itemBinders(itemBinders)

        if (providerAdapter is BaseQuickAdapter<*, *>) {
            emptyView = listHost.providerEmptyView(recyclerView.context)
                ?: EmptyView(recyclerView.context)
            emptyView.setViewModel(RefreshEmptyViewModel())

            providerAdapter.setEmptyView(emptyView as View)
            providerAdapter.headerWithEmptyEnable = true
            providerAdapter.footerWithEmptyEnable = true
        }
    }
}