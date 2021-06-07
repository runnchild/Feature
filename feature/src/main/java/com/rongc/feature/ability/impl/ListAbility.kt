package com.rongc.feature.ability.impl

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rongc.feature.R
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ability.IRecyclerList
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

class ListAbility(private val host: IHost<*>, private val listHost: IRecyclerList) : IAbility {

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

            vm._autoRefresh = listHost.autoRefresh()
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

//        val callback = BrvahListUpdateCallback(providerAdapter)
//        (vm?.items as? ObservableArrayList<Any>)?.addOnListChangedCallback(object :
//            ObservableList.OnListChangedCallback<ObservableList<Any>>() {
//            override fun onChanged(sender: ObservableList<Any>?) {
//                providerAdapter.notifyDataSetChanged()
//            }
//
//            override fun onItemRangeChanged(
//                sender: ObservableList<Any>?,
//                positionStart: Int,
//                itemCount: Int
//            ) {
//                callback.onChanged(positionStart, itemCount, null)
//            }
//
//            override fun onItemRangeInserted(
//                sender: ObservableList<Any>?,
//                positionStart: Int,
//                itemCount: Int
//            ) {
//                callback.onInserted(positionStart, itemCount)
//            }
//
//            override fun onItemRangeMoved(
//                sender: ObservableList<Any>?,
//                fromPosition: Int,
//                toPosition: Int,
//                itemCount: Int
//            ) {
//                callback.onMoved(fromPosition, toPosition)
//            }
//
//            override fun onItemRangeRemoved(
//                sender: ObservableList<Any>,
//                positionStart: Int,
//                itemCount: Int
//            ) {
//                callback.onRemoved(positionStart, itemCount)
//            }
//        })
    }
}