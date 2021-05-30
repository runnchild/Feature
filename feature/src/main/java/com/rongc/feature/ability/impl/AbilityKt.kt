package com.rongc.feature.ability.impl

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.Status

private fun IHost<*>.findProgressAbility(): AbsProgressAbility {
    return findAbility {
        it is AbsProgressAbility
    } as? AbsProgressAbility
        ?: throw IllegalArgumentException("need to register ProgressAbility first!!!")
}

fun IHost<*>.showProgressIfLoading(resource: Resource<*>?) {
    findProgressAbility().showProgressIfLoading(resource)
}

fun IHost<*>.showProgress() {
    findProgressAbility().showDialog()
}

fun IHost<*>.dismissProgress() {
    findProgressAbility().dismissDialog()
}

/**
 * 订阅列表数据结果返回监听，当结果返回时刷新列表
 * 订阅前应先注册{@link ListAbility}，如果ViewModel继承的是BaseListViewModel,则在注册后会自动订阅。
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
                    adapter.setDiffNewData(resource.data?.toMutableList())
                } else if (adapter is ListAdapter<*, *>) {
                    adapter as ListAdapter<T, *>
                    adapter.submitList(resource.data)
                }
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