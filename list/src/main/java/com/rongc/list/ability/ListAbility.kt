package com.rongc.list.ability

import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.list.ItemDecoration
import com.rongc.list.R
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.binding.itemBinders
import com.rongc.list.binding.itemDecoration
import com.rongc.list.widget.IEmptyView
import java.util.*

class ListAbility(viewModel: BaseViewModel, private val recyclerHost: IRecyclerHost) :
    ListAbilityIml(viewModel, recyclerHost) {

    init {
        val recyclerView = recyclerHost.recyclerView
        recyclerView.layoutManager = recyclerHost.providerLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter

        @Suppress("UNCHECKED_CAST")
        val call = recyclerView.getTag(R.id.tag_adapter_callback)
                as? (RecyclerView.Adapter<*>) -> Unit
        call?.invoke(adapter)
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
}