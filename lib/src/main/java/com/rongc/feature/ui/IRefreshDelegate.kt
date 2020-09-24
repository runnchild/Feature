package com.rongc.feature.ui

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.R
import com.rongc.feature.binding.itemBinders
import com.rongc.feature.binding.setup
import com.rongc.feature.binding.setupEmptyView
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.viewmodel.BaseRefreshViewModel
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.viewmodel.RefreshEmptyViewModel

/**
 * Activity和Fragment带刷新功能的代理类
 * 如果页面要支持刷新，需要继承此接口
 * 如果页面是标准的一个列表加刷新和加载功能，页面binding()中返回通用的{@link #BaseRefreshLayoutBinding)
 * 如果需要自定义页面，返回自定义页面，但是RecyclerView的id必须是 R.id.base_recyclerView, 如果需要SmartRefreshLayout,
 * 具体配置也可参考base_refresh_layout.xml
 */
interface IRefreshDelegate {

    fun init(viewModel: BaseViewModel<*>, owner: LifecycleOwner, view: View) {
        val recyclerView = view.findViewById<RecyclerView?>(R.id.base_recyclerView)
            ?: throw NullPointerException("必须有id为base_recyclerView的RecyclerView")

        recyclerView.layoutManager = providerLayoutManager(view.context)
        recyclerView.setTag(R.id.layout_isBinding, this is IBinding<*>)

        recyclerView.setup(providerAdapter())

        val baseRefreshViewModel = viewModel as? BaseRefreshViewModel<*, *>
        baseRefreshViewModel?.itemBinders?.observe(owner, Observer {
            @Suppress("UNCHECKED_CAST")
            recyclerView.itemBinders(it as MutableList<BaseRecyclerItemBinder<Any>>)
        })
        baseRefreshViewModel?.autoRefresh = autoRefresh()

        val emptyViewModel = recyclerView.setupEmptyView()?.apply {
            baseRefreshViewModel?.emptyRefreshViewModel = this
        }
        baseRefreshViewModel?.setupEmptyView?.observe(owner, Observer {
            when (it) {
                RefreshEmptyViewModel.EMPTY_NET_DISCONNECT -> {
                    emptyViewModel?.showNoNet()
                }
                RefreshEmptyViewModel.EMPTY_NET_UNAVAILABLE -> {
                    emptyViewModel?.showNetUnavailable()
                }
                else -> {
                    val builder = EmptyBuilder().apply(setupEmptyView(it)).apply {
                        if (refreshClick == null) {
                            refreshClick = {
                                baseRefreshViewModel.refresh()
                            }
                        }
                    }

                    emptyViewModel?.builder(builder)
                }
            }
        })
    }

    /**
     * 默认返回LinearLayoutManager
     * 如果需要其他的LayoutManager，重写此方法
     */
    fun providerLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    /**
     * 如果需要提供新Adapter, 重写此方法放回需要设置的Adapter
     */
    fun providerAdapter(): BaseQuickAdapter<Any, BaseViewHolder>? = null

    /**
     * 是否进入页面自动刷新
     */
    fun autoRefresh() = true

    fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit = {}
}