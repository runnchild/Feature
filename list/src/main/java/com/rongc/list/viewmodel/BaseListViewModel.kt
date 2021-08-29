package com.rongc.list.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.vo.*
import com.rongc.list.PageIndicator
import com.rongc.list.ability.emptyState
import com.rongc.list.binding.LoadStatus
import com.runnchild.emptyview.EmptyState
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

const val REFRESH_ONCE = 1
const val REFRESH_ALWAYS = 2
const val REFRESH_NON = 0

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

    internal var _autoRefresh: Boolean = false
        set(value) {
            // 页面设置了自动刷新，并且之前没刷新过时自动刷新
            if (!field && value || reenterRefreshMode != REFRESH_NON || resource.isError) {
                refresh()
                if (reenterRefreshMode == REFRESH_ONCE) {
                    reenterRefreshMode = REFRESH_NON
                }
            }
            field = value
        }

    /**
     * 无论之前是否发起过请求，每次回到页面(onCreate或者onCreateView)都将重新发起新请求
     */
    var reenterRefreshMode = REFRESH_NON
//        set(value) {
//            if (value == REFRESH_ONCE) {
//                field = REFRESH_NON
//            }
//            field = value
//        }

    val setupEmptyView = MutableLiveData<EmptyState>()

    private val _request = MutableLiveData<Int>()

    private val _result: LiveData<Resource<List<T>>> = _request.switchMap {
        loadListData(it).run {
            if (it == PageIndicator.PAGE_START) {
                result.value?.data?.let {
                    doOnStart { loading ->
                        if (loading.data != null) {
                            loading
                        } else {
                            Resource.loading(result.value?.data)
                        }
                    }
                } ?: this
            } else {
                this
            }
        }
    }

    val isRefresh get() = _request.value == PageIndicator.PAGE_START

    val listLiveData = MediatorLiveData<List<T>>()

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

    val items get() = listLiveData.value as? ArrayList ?: arrayListOf()

    internal var updateCallback: ListUpdateCallback? = null

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

    init {
        listLiveData.addSource(result) {
            if (it.isSuccess) {
                val items = it.data ?: arrayListOf()
                if (isRefresh) {
                    pageIndicator.revert()
                    listLiveData.value = items
                } else {
                    if (!it.data.isNullOrEmpty()) {
                        pageIndicator.next()
                        val pos = this.items.size
                        this.items.addAll(items)
                        updateCallback?.onInserted(pos, items.size)
                    }
                }
            }
        }
    }

    /**
     * 刷新
     * @param byPull 是否通过下拉的刷新
     */
    fun refresh(byPull: Boolean = false) {
        loadData(PageIndicator.PAGE_START)
    }

    fun loadMore() {
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

    fun remove(item: T) {
        removeAt(items.indexOf(item))
    }

    fun removeAt(pos: Int) {
        if (pos > -1) {
            items.removeAt(pos)
            updateCallback?.onRemoved(pos, 1)
        }
    }

    fun addData(data: T, index: Int = items.size) {
        items.add(index, data)
        updateCallback?.onInserted(index, 1)
    }

    fun removeLastOrNull() {
        items.removeLastOrNull()?.let {
            updateCallback?.onRemoved(items.size, 1)
        }
    }

    fun setData(index: Int, data: T, payload: Any? = null) {
        items[index] = data
        updateCallback?.onChanged(index, 1, payload)
    }

    fun updateData(data: T): Int {
        val index = items.indexOf(data)
        if (index > -1) {
            setData(index, data)
        }
        return index
    }

    fun notifyData() {
        listLiveData.value = listLiveData.value
    }

    fun clear() {
        val count = items.size
        items.clear()
        updateCallback?.onRemoved(0, count)
    }

    private fun List<T>?.toMutableList(): ArrayList<T> {
        return if (this is MutableList) {
            this as ArrayList
        } else {
            if (this == null) {
                arrayListOf()
            } else {
                ArrayList(this)
            }
        }
    }
}