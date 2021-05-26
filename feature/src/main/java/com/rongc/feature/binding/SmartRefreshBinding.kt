package com.rongc.feature.binding

import androidx.databinding.BindingAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * 下拉刷新监听和加载更多监听
 */
@BindingAdapter("refreshListener", "loadMoreListener", requireAll = false)
fun SmartRefreshLayout.onRefreshListener(
    onRefreshListener: OnRefreshListener?,
    loadMoreListener: OnLoadMoreListener?
) {
    setOnRefreshListener(onRefreshListener)
    setOnLoadMoreListener(loadMoreListener)
}

/**
 * 是否支持下拉刷新和加载更多
 */
@BindingAdapter("enableLoadMore", "enableRefresh", requireAll = false)
fun SmartRefreshLayout.enable(enableLoadMore: Boolean = true, enableRefresh: Boolean = true) {
    setEnableLoadMore(enableLoadMore)
    setEnableRefresh(enableRefresh)
}

/**
 * 设置刷新/加载状态变更
 */
@BindingAdapter("loadStatus")
fun SmartRefreshLayout.status(status: LoadStatus?) {
    when (status) {
        LoadStatus.FINISH_REFRESH_SUCCESS -> finishRefresh(true)
        LoadStatus.FINISH_REFRESH_FAILED -> finishRefresh(false)
        LoadStatus.FINISH_LOAD_SUCCESS -> finishLoadMore(true)
        LoadStatus.FINISH_LOAD_FAILED -> finishLoadMore(false)
        LoadStatus.FINISH_LOAD_NO_MORE -> finishLoadMoreWithNoMoreData()
    }
}

/**
 * 是否支持进入页面自动刷新
 */
@BindingAdapter("autoRefresh")
fun SmartRefreshLayout.autoRefresh(autoRefresh: Boolean) {
    if (autoRefresh) {
        autoRefresh()
    }
}

enum class LoadStatus {
    FINISH_REFRESH_SUCCESS,
    FINISH_REFRESH_FAILED,
    FINISH_LOAD_SUCCESS,
    FINISH_LOAD_FAILED,
    FINISH_LOAD_NO_MORE,
    LOADING
}