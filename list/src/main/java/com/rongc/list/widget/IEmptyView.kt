package com.rongc.list.widget

import com.rongc.list.viewmodel.EmptyViewConfig

interface IEmptyView {

    fun setViewModel(config: EmptyViewConfig)

    fun getViewModel(): EmptyViewConfig?
}