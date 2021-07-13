package com.runnchild.emptyview

interface IEmptyView {

    fun setConfig(config: EmptyViewConfig)

    fun getViewModel(): EmptyViewConfig?
}