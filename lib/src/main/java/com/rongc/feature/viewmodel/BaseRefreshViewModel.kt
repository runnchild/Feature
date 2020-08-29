package com.rongc.feature.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rongc.feature.binding.LoadStatus
import com.rongc.feature.model.BaseModel
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.refresh.DataRequestCallback
import com.rongc.feature.refresh.PageIndicator
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

/**
 * 支持下拉刷新ViewModel，维护上拉和加载更多数据请求及ui业务
 *
 */
abstract class BaseRefreshViewModel<T, M : BaseModel> : BaseViewModel<M>() {
    /**
     * 请求页码
     */
    private val pageIndicator by lazy {
        PageIndicator()
    }

    /**
     * 是否支持加载更多
     */
    var enableLoadMore = ObservableBoolean(true)

    /**
     * 是否支持下拉刷新
     */
    var enableRefresh = ObservableBoolean(true)

    val onRefreshListener = OnRefreshListener {
        refresh()
    }

    val onLoadMoreListener = OnLoadMoreListener {
        loadMore()
    }

    /**
     * 刷新和加载状态
     */
    val loadStatus = ObservableField<LoadStatus>()

    /**
     * 是否自动刷新
     */
    var autoRefresh = false

    private val dataRequestCall = object : DataRequestCallback<List<T>> {
        override fun onSuccess(page: Int, data: List<T>) {
            if (page == PageIndicator.PAGE_START) {
                items.clear()
                items.addAll(data)
                pageIndicator.revert()
                setStatus(LoadStatus.FINISH_REFRESH_SUCCESS)
            } else {
                if (!data.isNullOrEmpty()) {
                    pageIndicator.next()
                    items.addAll(data)
                    setStatus(LoadStatus.FINISH_LOAD_SUCCESS)
                } else {
                    setStatus(LoadStatus.FINISH_LOAD_NO_MORE)
                }
            }
        }

        override fun onFailed(page: Int) {
            if (page == PageIndicator.PAGE_START) {
                setStatus(LoadStatus.FINISH_REFRESH_FAILED)
            } else {
                setStatus(LoadStatus.FINISH_LOAD_FAILED)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val binders = mutableSetOf<BaseRecyclerItemBinder<out T>>()
        providerItemBinders(binders)
        binders.forEach {
            itemBinders.value?.add(it)
        }
    }

    /**
     * 提供列表的ItemHolder， 不同布局可添加不同holder
     */
    abstract fun providerItemBinders(binders: MutableSet<BaseRecyclerItemBinder<out T>>)

    private fun setStatus(status: LoadStatus) {
        val same = loadStatus.get() == status
        loadStatus.set(status)
        if (same) {
            loadStatus.notifyChange()
        }
    }

    /**
     * 列表item类型， 由页面监听数组变化， 继承类不应直接操作
     */
    val itemBinders = MutableLiveData<MutableList<BaseRecyclerItemBinder<out T>>>().apply {
        value = mutableListOf()
    }

    /**
     * 列表item数据， 由页面监听数组变化， 继承类不应直接操作
     */
    val items = ObservableArrayList<T>()

    fun refresh() {
        loadData(PageIndicator.PAGE_START, dataRequestCall)
    }

    private fun loadMore() {
        loadData(pageIndicator.page + 1, dataRequestCall)
    }

    /**
     * 数据请求方法
     * @param page 请求页码
     * @param dataRequestCall 结果回调
     */
    abstract fun loadData(page: Int, dataRequestCall: DataRequestCallback<List<T>>)

}