package com.rongc.demo.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.rongc.feature.viewmodel.BaseViewModel

class HomeViewModel(application: Application, private val savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    val count: Int get() = savedStateHandle["savedField"] ?: savedStateHandle.set("savedField", 0)
        .let { savedStateHandle["savedField"]!! }

    fun setCount(count: Int) {
        savedStateHandle.set("savedField", count)
    }
}