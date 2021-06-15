package com.rongc.list.ability

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.list.ItemDecoration
import com.rongc.list.R
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.binding.itemBinders
import com.rongc.list.binding.itemDecoration
import java.util.*

class ListAbility(viewModel: BaseViewModel, private val recyclerHost: IRecyclerList) : ListAbilityIml(viewModel, recyclerHost) {

    override fun onCreate(owner: LifecycleOwner) {
        val recyclerView = recyclerHost.recyclerView
        recyclerView.layoutManager = recyclerHost.providerLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        super.onCreate(owner)

        @Suppress("UNCHECKED_CAST")
        val call = recyclerView.getTag(R.id.tag_adapter_callback)
                as? (RecyclerView.Adapter<*>) -> Unit
        call?.invoke(adapter)
    }

    override fun setupItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        recyclerHost.recyclerView.itemBinders(binders)
    }

    override fun setupItemDecoration(decoration: ItemDecoration) {
        recyclerHost.recyclerView.itemDecoration(decoration)
    }
}