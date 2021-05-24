package com.rongc.feature.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel() : ViewModel() {
    constructor(application: Application, stateHandle: SavedStateHandle):this()
}