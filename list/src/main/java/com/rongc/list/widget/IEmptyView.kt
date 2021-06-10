package com.rongc.list.widget

import com.rongc.list.viewmodel.RefreshEmptyViewModel

interface IEmptyView {

    fun setViewModel(viewModel: RefreshEmptyViewModel)

    fun getViewModel(): RefreshEmptyViewModel?
}