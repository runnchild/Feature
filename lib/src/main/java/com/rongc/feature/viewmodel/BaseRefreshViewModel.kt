package com.rongc.feature.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.rongc.feature.SingleLiveData
import com.rongc.feature.binding.LoadStatus
import com.rongc.feature.model.BaseModel
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.refresh.DataRequestCallback
import com.rongc.feature.refresh.PageIndicator
import com.rongc.feature.widget.ItemDecoration
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
    
    private var firstRefresh = true

    val onRefreshListener = OnRefreshListener {
        // 开启了自动刷新的首次判断为非用户操作
        refresh(!(autoRefresh && firstRefresh))
        firstRefresh = false
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

    var emptyRefreshViewModel: RefreshEmptyViewModel? = null

    val setupEmptyView = SingleLiveData<Int>()

    /**
     * 没有数据时是否允许刷新
     */
    var enableRefreshWhenEmpty = true

    private val dataRequestCall = object : DataRequestCallback<List<T>> {
        var refreshByUser = false
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

            if (items.isEmpty()) {
                setupEmptyView.value = RefreshEmptyViewModel.EMPTY_EMPTY

                if (!enableRefreshWhenEmpty) {
                    enableRefresh.set(false)
                }
            }
        }

        override fun onFailed(page: Int) {
            if (page == PageIndicator.PAGE_START) {
                setStatus(LoadStatus.FINISH_REFRESH_FAILED)
            } else {
                setStatus(LoadStatus.FINISH_LOAD_FAILED)
            }
            if (!NetworkUtils.isConnected()) {
                setupEmptyView.value = RefreshEmptyViewModel.EMPTY_NET_DISCONNECT
            } else if (!NetworkUtils.isAvailable()) {
                setupEmptyView.value = RefreshEmptyViewModel.EMPTY_NET_UNAVAILABLE
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
        if (autoRefresh) {
            refresh()
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

    val itemDecoration: ItemDecoration
        get() = ItemDecoration.Builder().apply(decorationBuilder()).build()

    open fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return { }
    }

    /**
     * 刷新
     * @param byPull 是否通过下拉的刷新
     */
    fun refresh(byPull: Boolean = false) {
        dataRequestCall.refreshByUser = byPull
        loadData(PageIndicator.PAGE_START, dataRequestCall)
    }

    fun loadMore() {
        dataRequestCall.refreshByUser = true
        loadData(pageIndicator.page + 1, dataRequestCall)
    }

    /**
     * 数据请求方法
     * @param page 请求页码
     * @param dataRequestCall 结果回调
     */
    abstract fun loadData(page: Int, dataRequestCall: DataRequestCallback<List<T>>)

}