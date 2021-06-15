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
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.Status
import com.rongc.list.ItemDecoration
import com.rongc.list.R
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.adapter.BinderAdapter
import com.rongc.list.binding.itemBinders
import com.rongc.list.binding.itemDecoration
import com.rongc.list.setCompatDiffNewData
import com.rongc.list.viewmodel.BaseListViewModel
import com.rongc.list.viewmodel.DefaultEmptyConfig
import com.rongc.list.viewmodel.EmptyBuilder
import com.rongc.list.viewmodel.RefreshEmptyViewModel
import com.rongc.list.widget.EmptyView
import com.rongc.list.widget.IEmptyView

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
//            providerAdapter.headerWithEmptyEnable = true
//            providerAdapter.footerWithEmptyEnable = true
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

/**
 * 订阅列表数据结果返回监听，当结果返回时刷新列表
 * 订阅前应先注册{@link ListAbility}，如果ViewModel继承的是BaseListViewModel,则在注册后会自动订阅。
 * 否则需手动调用
 * 否则需手动调用
 */
@Suppress("UNCHECKED_CAST")
fun <T> IHost<*>.observeResource(result: LiveData<Resource<List<T>>>) {
    findAbility { it is ListAbility }?.let {
        it as ListAbility
        result.observe(lifecycleOwner) { resource ->
            if (resource.status != Status.LOADING || resource.data != null) {
                val adapter = it.adapter
                if (adapter is BaseQuickAdapter<*, *>) {
                    adapter as BaseQuickAdapter<T, BaseViewHolder>
                    adapter.setCompatDiffNewData(resource.data)
                } else if (adapter is ListAdapter<*, *>) {
                    adapter as ListAdapter<T, *>
                    adapter.submitList(resource.data)
                }
            }
        }
    }
}