package com.rongc.feature.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.rongc.feature.api.ApiResponse
import com.rongc.feature.api.ApiSuccessResponse
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.isLoading

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/5/28
 */
abstract class NetOnlyResource<T> : NetworkBoundResource<T, T>() {
    private var resource: T? = null

    private val dbLiveData get() = liveData<T> { emit(resource!!) }

    override fun saveCallResult(item: T) {
        resource = item
    }

    override fun shouldFetch(data: T?) = true

    override fun loadFromDb(): LiveData<T> {
        return if (resource == null) {
            AbsentLiveData.create()
        } else {
            dbLiveData
        }
    }
}

/**
 * 只发起网络请求，不缓存
 */
fun <T> LiveData<ApiResponse<T>>.networkOnly(
    process: ((ApiSuccessResponse<T>) -> T)? = null,
    failed: (() -> Unit)? = null
): LiveData<Resource<T>> {
    return object : NetOnlyResource<T>() {
        override fun createCall(): LiveData<ApiResponse<T>> {
            return this@networkOnly
        }

        override fun processResponse(response: ApiSuccessResponse<T>): T {
            return process?.invoke(response) ?: super.processResponse(response)
        }

        override fun onFetchFailed() {
            failed?.invoke()
        }
    }.asLiveData()
}

/**
 * 忽略加载中状态，只接收结果通知
 */
fun <T> LiveData<Resource<T>>.ignoreLoading(): LiveData<Resource<T>> {
    val liveData = MediatorLiveData<Resource<T>>()
    liveData.addSource(this) {
        if (!it.isLoading) {
            liveData.value = it
        }
    }
    return liveData
}

fun <T> LiveData<Resource<T>>.ignoreLoading(
    owner: LifecycleOwner, observer: Observer<Resource<T>>
) {
    observe(owner) {
        if (!it.isLoading) {
            observer.onChanged(it)
        }
    }
}