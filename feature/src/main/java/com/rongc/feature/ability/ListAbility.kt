package com.rongc.feature.ability

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.viewmodel.BaseListViewModel

class ListAbility(val host: IHost<*>, private val listAbility: IListAbility) : IAbility {

    lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreate(owner: LifecycleOwner) {
        val vm = host.viewModel as BaseListViewModel<*>
        host.observeResource(vm)

        val recyclerView = listAbility.recyclerView
        recyclerView.layoutManager = listAbility.providerLayoutManager(recyclerView.context)
        val providerAdapter = listAbility.providerAdapter() ?: BaseBinderAdapter()
        adapter = providerAdapter
        recyclerView.adapter = providerAdapter
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> IHost<*>.observeResource(viewModel: BaseListViewModel<T>) {
        findAbility { it is ListAbility }?.let {
            it as ListAbility
            viewModel.result.observe(lifecycleOwner) { resource ->
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

    private fun <T> List<T>.toMutableList(): ArrayList<T> {
        return if (this is ArrayList<*>) {
            this as ArrayList<T>
        } else {
            ArrayList(this)
        }
    }
}