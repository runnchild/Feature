package com.runnchild.feature.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    constructor(application: Application, stateHandle: SavedStateHandle) : this(application)
}