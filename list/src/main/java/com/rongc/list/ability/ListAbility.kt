package com.rongc.list.ability

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.list.ItemDecoration
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.binding.itemBinders
import com.rongc.list.binding.itemDecoration
import com.runnchild.emptyview.IEmptyView

class ListAbility(viewModel: BaseViewModel, private val recyclerHost: IRecyclerHost) :
    AbsListAbility(viewModel, recyclerHost) {

    init {
        val recyclerView = recyclerHost.recyclerView
        recyclerView.layoutManager = recyclerHost.providerLayoutManager(recyclerView.context)
    }

    override fun <T> onFetchData(adapter: RecyclerView.Adapter<*>, list: List<T>?) {
        super.onFetchData(adapter, list)
        val recyclerView = recyclerHost.recyclerView
        if (recyclerView.adapter == adapter) {
            return
        }
        recyclerView.adapter = adapter
        invokeDoOnAdapter(recyclerView, adapter)
    }

    override fun providerEmptyView(): IEmptyView? {
        return recyclerHost.providerEmptyView(recyclerHost.recyclerView.context)
    }

    override fun setupItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        recyclerHost.recyclerView.itemBinders(binders)
    }

    override fun setupItemDecoration(decoration: ItemDecoration) {
        recyclerHost.recyclerView.itemDecoration(decoration)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        recyclerHost.recyclerView.adapter = null
    }
}