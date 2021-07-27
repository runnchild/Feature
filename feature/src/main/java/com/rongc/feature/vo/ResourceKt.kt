package com.rongc.feature.vo

import androidx.lifecycle.LiveData
import androidx.lifecycle.map

fun <T> LiveData<Resource<T>>.doOnNext(block: (Resource<T>)->Unit): LiveData<Resource<T>> {
    return map {
        block(it)
        it
    }
}

fun <T> LiveData<Resource<T>>.doOnSuccess(block: (Resource<T>)->Unit): LiveData<Resource<T>> {
    return map {
        if (it.isSuccess) {
            block(it)
        }
        it
    }
}

fun <T> LiveData<Resource<T>>.doOnError(block: (Resource<T>)->Unit): LiveData<Resource<T>> {
    return map {
        if (it.isError) {
            block(it)
        }
        it
    }
}