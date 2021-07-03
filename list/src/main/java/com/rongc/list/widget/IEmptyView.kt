package com.rongc.list.widget

import com.rongc.list.viewmodel.EmptyViewConfig

interface IEmptyView {

    fun setConfig(config: EmptyViewConfig)

    fun getViewModel(): EmptyViewConfig?
}