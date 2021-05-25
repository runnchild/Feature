package com.rongc.feature.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel() : ViewModel() {

    // 如果需要savedStateHandle， 使用此构造
    constructor(savedStateHandle: SavedStateHandle) : this()
}