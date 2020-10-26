package com.rongc.feature.widget

import com.rongc.feature.viewmodel.RefreshEmptyViewModel

interface IEmptyView {

    fun setViewModel(viewModel: RefreshEmptyViewModel)

    fun getViewModel(): RefreshEmptyViewModel
}