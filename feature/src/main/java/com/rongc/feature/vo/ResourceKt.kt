package com.rongc.feature.vo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map

fun <T> LiveData<Resource<T>>.doOnNext(block: (Resource<T>)->Unit): LiveData<Resource<T>> {
    return map {
        block(it)
        it
    }
}

fun <T> LiveData<Resource<T>>.doOnSuccess(block: (Resource<T>) -> Unit): LiveData<Resource<T>> {
    return map {
        if (it.isSuccess) {
            block(it)
        }
        it
    }
}

fun <T> LiveData<Resource<T>>.doOnError(block: (Resource<T>) -> Unit): LiveData<Resource<T>> {
    return map {
        if (it.isError) {
            block(it)
        }
        it
    }
}

fun <T> LiveData<Resource<T>>.doOnStart(block: (Resource<T>) -> Resource<T>): LiveData<Resource<T>> {
    return map {
        if (it.isLoading) {
            block(it)
        } else {
            it
        }
    }
}

/**
 * 只在成功时接收通知
 */
fun <T> LiveData<Resource<T>>.observeOnSuccess(
    owner: LifecycleOwner, observer: Observer<Resource<T>>
) {
    observe(owner) {
        if (it?.isSuccess == true) {
            observer.onChanged(it)
        }
    }
}