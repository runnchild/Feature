package com.rongc.list.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.Status
import com.rongc.feature.vo.isSuccess
import com.rongc.list.PageIndicator
import com.rongc.list.ability.emptyState
import com.rongc.list.binding.LoadStatus
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * 支持下拉刷新ViewModel，维护上拉和加载更多数据请求及ui业务
 *
 */
abstract class BaseListViewModel<T> : BaseViewModel() {
    /**
     * 请求页码
     */
    private val pageIndicator by lazy {
        PageIndicator()
    }

    /**
     * 是否支持加载更多
     */
    val enableLoadMore = ObservableBoolean(true)

    /**
     * 是否支持下拉刷新
     */
    val enableRefresh = ObservableBoolean(true)

    private var firstRefresh = true

    val onRefreshListener = OnRefreshListener {
        // 开启了自动刷新的首次判断为非用户操作
        refresh(!firstRefresh)
        firstRefresh = false
    }

    val onLoadMoreListener = OnLoadMoreListener {
        loadMore()
    }

    val autoRefresh = ObservableBoolean(false)
    var _autoRefresh: Boolean = false
        set(value) {
            // 页面设置了自动刷新，并且之前没刷新过时自动刷新
            if (!field && value) {
//                if (enableRefresh.get()) {
//                    autoRefresh.set(true)
//                } else {
//                }
//                autoRefresh.set(true)
                refresh()
            }
            field = value
        }

    val setupEmptyView = MutableLiveData<com.runnchild.emptyview.EmptyState>()

    private val _request = MutableLiveData<Int>()

    private val _result: LiveData<Resource<List<T>>> = _request.switchMap {
        loadListData(it)
    }

    val isRefresh get() = _request.value == PageIndicator.PAGE_START

    val result = _result.map {
        if (it.isSuccess) {
            if (isRefresh) {
                pageIndicator.revert()
            } else {
                if (!it.data.isNullOrEmpty()) {
                    pageIndicator.next()
                }
            }
        }

        it.emptyState { state ->
            setupEmptyView.postValue(state)
        }
        it
    }

    //    val items get() = result.value?.data
    /**
     * 此时的接口实时状态
     */
    val resource get() = result.value

    /**
     * 刷新和加载状态
     */
    val loadStatus = _result.map { resource ->
        when (resource.status) {
            Status.SUCCESS -> {
                if (isRefresh) {
                    LoadStatus.FINISH_REFRESH_SUCCESS
                } else {
                    if (resource.data.isNullOrEmpty()) {
                        LoadStatus.FINISH_LOAD_NO_MORE
                    } else {
                        LoadStatus.FINISH_LOAD_SUCCESS
                    }
                }
            }
            Status.ERROR -> {
                if (isRefresh) {
                    LoadStatus.FINISH_REFRESH_FAILED
                } else {
                    LoadStatus.FINISH_LOAD_FAILED
                }
            }
            Status.LOADING -> {
                LoadStatus.LOADING
            }
        }
    }

    /**
     * 没有数据时是否允许刷新
     */
    var enableRefreshWhenEmpty = true

    /**
     * 刷新
     * @param byPull 是否通过下拉的刷新
     */
    fun refresh(byPull: Boolean = false) {
        loadData(PageIndicator.PAGE_START)
    }

    private fun loadMore() {
        loadData(pageIndicator.page + 1)
    }

    /**
     * 数据请求方法
     * @param page 请求页码
     */
    open fun loadData(page: Int) {
        _request.value = page
    }

    abstract fun loadListData(page: Int): LiveData<Resource<List<T>>>
}