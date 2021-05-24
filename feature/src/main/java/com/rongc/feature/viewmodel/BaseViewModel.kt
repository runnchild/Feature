package com.rongc.feature.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.blankj.utilcode.util.Utils

abstract class BaseViewModel(application: Application = Utils.getApp()) : AndroidViewModel(application) {

    // 如果需要savedStateHandle， 使用此构造
    constructor(application: Application, savedStateHandle: SavedStateHandle) : this(application)
}