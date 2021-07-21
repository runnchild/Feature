package com.rongc.feature.viewmodel

import androidx.lifecycle.*
import com.rongc.feature.vo.Resource
import kotlinx.coroutines.*

abstract class BaseViewModel() : ViewModel() {

    // 如果需要savedStateHandle， 使用此构造
    constructor(savedStateHandle: SavedStateHandle) : this()

    fun <T> launch(scope: suspend (coroutineScope: CoroutineScope) -> T): LiveData<Resource<T>> {
        val result = MutableLiveData<Resource<T>>()
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            result.value = Resource.error(e, null)
        }
        viewModelScope.launch(exceptionHandler) {
            result.value = Resource.loading(null)
            try {
                val data = scope(this)
                result.value = Resource.success(data)
            } catch (e: CancellationException) {
                e.printStackTrace()
                result.value = Resource.error(e, null)
            }
        }
        return result
    }
}

val CoroutineScope.asJob get() = coroutineContext[Job]
