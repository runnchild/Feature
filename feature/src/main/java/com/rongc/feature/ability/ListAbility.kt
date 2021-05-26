package com.rongc.feature.ability

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.R
import com.rongc.feature.binding.itemDecoration
import com.rongc.feature.refresh.ItemDecoration
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.vo.Resource

class ListAbility(private val host: IHost<*>, private val listHost: IListAbility) : IAbility {

    lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreate(owner: LifecycleOwner) {
        @Suppress("UNCHECKED_CAST")
        val vm = host.viewModel as? BaseListViewModel<Any>
        // 如果非BaseListViewModel则需要手动调用observeResource
        if (vm != null) {
            host.observeResource(vm.result)
        }

        val recyclerView = listHost.recyclerView
        recyclerView.layoutManager = listHost.providerLayoutManager(recyclerView.context)
        val providerAdapter = listHost.providerAdapter() ?: BaseBinderAdapter()
        adapter = providerAdapter
        recyclerView.adapter = providerAdapter
        @Suppress("UNCHECKED_CAST")
        val call = recyclerView.getTag(R.id.tag_adapter_callback) as? (RecyclerView.Adapter<*>) -> Unit
        call?.invoke(adapter)

        val decoration = ItemDecoration.Builder().apply(listHost.decorationBuilder()).build()
        recyclerView.itemDecoration(decoration)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> IHost<*>.observeResource(result: LiveData<Resource<List<T>>>) {
    findAbility { it is ListAbility }?.let {
        it as ListAbility
        result.observe(lifecycleOwner) { resource ->
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