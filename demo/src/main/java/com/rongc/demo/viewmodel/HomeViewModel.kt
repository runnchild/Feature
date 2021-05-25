package com.rongc.demo.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.rongc.feature.viewmodel.BaseViewModel

class HomeViewModel(private val savedStateHandle: SavedStateHandle) :
    BaseViewModel(savedStateHandle) {

    val count: Int
        get() = savedStateHandle["savedField"] ?: savedStateHandle.set("savedField", 0)
            .let { savedStateHandle["savedField"]!! }

    fun setCount(count: Int) {
        savedStateHandle.set("savedField", count)
    }
}