package com.rongc.feature.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.rongc.feature.binding.LoadStatus
import com.rongc.feature.bus.SingleLiveData
import com.rongc.feature.refresh.PageIndicator
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.Status
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.coroutines.Job

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

//    var emptyRefreshViewModel: RefreshEmptyViewModel? = null

    val setupEmptyView = SingleLiveData<Int>()

    val result = MediatorLiveData<Resource<List<T>>>()

    /**
     * 没有数据时是否允许刷新
     */
    var enableRefreshWhenEmpty = true

//    private val dataRequestCall = RequestCallback()

    var primaryJob: Job? = null

//    inner class RequestCallback : DataRequestCallback<List<T>> {
//        var refreshByUser = false
//        override fun onSuccess(page: Int, data: List<T>) {
//            if (page == PageIndicator.PAGE_START) {
//                items.clear()
//                items.addAll(data)
//                pageIndicator.revert()
//                setStatus(LoadStatus.FINISH_REFRESH_SUCCESS)
//            } else {
//                if (!data.isNullOrEmpty()) {
//                    pageIndicator.next()
//                    items.addAll(data)
//                    setStatus(LoadStatus.FINISH_LOAD_SUCCESS)
//                } else {
//                    setStatus(LoadStatus.FINISH_LOAD_NO_MORE)
//                }
//            }
//
//            if (items.isEmpty()) {
//                setupEmptyView.value = RefreshEmptyViewModel.EMPTY_EMPTY
//
//                if (!enableRefreshWhenEmpty) {
//                    enableRefresh.set(false)
//                }
//            }
//        }
//
//        override fun onFailed(page: Int) {
//            if (page == PageIndicator.PAGE_START) {
//                setStatus(LoadStatus.FINISH_REFRESH_FAILED)
//            } else {
//                setStatus(LoadStatus.FINISH_LOAD_FAILED)
//            }
//            thread {
//                if (!NetworkUtils.isConnected()) {
//                    setupEmptyView.postValue(RefreshEmptyViewModel.EMPTY_NET_DISCONNECT)
//                } else if (!NetworkUtils.isAvailable()) {
//                    setupEmptyView.postValue(RefreshEmptyViewModel.EMPTY_NET_UNAVAILABLE)
//                }
//            }
//        }
//    }

//    override fun onCreate() {
//        super.onCreate()
    // 刷新时机延后，到UI的initView，initData方法后
//        viewModelScope.launch(Dispatchers.Main) {
//            if (autoRefresh) {
//                refresh()
//            }
//        }
//    }

    fun setStatus(status: LoadStatus) {
        val same = loadStatus.get() == status
        loadStatus.set(status)
        if (same) {
            loadStatus.notifyChange()
        }
    }

    /**
     * 列表item数据， 由页面监听数组变化， 继承类不应直接操作
     */
    val items = ObservableArrayList<T>()

    /**
     * 刷新
     * @param byPull 是否通过下拉的刷新
     */
    fun refresh(byPull: Boolean = false) {
//        dataRequestCall.refreshByUser = byPull
        loadData(PageIndicator.PAGE_START)
    }

    fun loadMore() {
//        dataRequestCall.refreshByUser = true
        loadData(pageIndicator.page + 1)
    }

    /**
     * 数据请求方法
     * @param page 请求页码
     */
    open fun loadData(page: Int) {
        val source = loadListData(page)
        result.addSource(source) {
            result.removeSource(source)
            loadStatus(it)?.let { status ->
                loadStatus.set(status)
            }
        }
    }

    private fun <T> loadStatus(resource: Resource<List<T>>?): LoadStatus? {
        val isRefresh = getPage() == PageIndicator.PAGE_START
        resource ?: return if (isRefresh) {
            LoadStatus.FINISH_REFRESH_FAILED
        } else {
            LoadStatus.FINISH_LOAD_FAILED
        }

        return if (resource.status == Status.SUCCESS) {
            if (isRefresh) {
                LoadStatus.FINISH_REFRESH_SUCCESS
            } else {
                if (resource.data.isNullOrEmpty()) {
                    LoadStatus.FINISH_LOAD_NO_MORE
                } else {
                    LoadStatus.FINISH_LOAD_SUCCESS
                }
            }
        } else if (resource.status == Status.ERROR) {
            if (isRefresh) {
                LoadStatus.FINISH_REFRESH_FAILED
            } else {
                LoadStatus.FINISH_LOAD_FAILED
            }
        } else null
    }

    open fun loadDataSuccess(page: Int, data: List<T>) {
    }

//    open fun loadDataFailed(page: Int, error: ServicesException) {
//    }

    abstract fun loadListData(page: Int): LiveData<Resource<List<T>>>

    fun getPage() = pageIndicator.page
}