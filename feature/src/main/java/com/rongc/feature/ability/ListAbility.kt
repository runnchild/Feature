package com.rongc.feature.ability

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.viewmodel.BaseListViewModel

fun <T> IHost<*>.observeResource(viewModel: BaseListViewModel<T>) {
    findAbility { it is ListAbility }?.let {
        it as ListAbility
        viewModel.result.observe(lifecycleOwner) { resource ->
            val adapter = it.adapter
            if (adapter is BaseQuickAdapter<*, *>) {
                adapter as BaseQuickAdapter<T, BaseViewHolder>
                adapter.getDiffer().submitList(resource.data?.toMutableList())
            } else if (adapter is ListAdapter<*, *>) {
                adapter as ListAdapter<T, *>
                adapter.submitList(resource.data)
            }
        }
    }
}

private fun <T> List<T>.toMutableList(): ArrayList<T> {
    return if (this is ArrayList<*>) {
        this as ArrayList<T>
    } else {
        ArrayList(this)
    }
}

class ListAbility(private val host: IListAbility, val recyclerView: RecyclerView) : IAbility {

    lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreate(owner: LifecycleOwner) {
        recyclerView.layoutManager = host.providerLayoutManager(recyclerView.context)
        val providerAdapter = host.providerAdapter() ?: BaseBinderAdapter()
        adapter = providerAdapter
        recyclerView.adapter = providerAdapter

    }
}