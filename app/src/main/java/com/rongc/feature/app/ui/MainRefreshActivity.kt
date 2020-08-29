package com.rongc.feature.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rongc.feature.app.viewmodel.MainRefreshViewModel
import com.rongc.feature.databinding.BaseRefreshLayoutBinding
import com.rongc.feature.ui.BaseBindingActivity
import com.rongc.feature.ui.IRefreshDelegate

class MainRefreshActivity : BaseBindingActivity<BaseRefreshLayoutBinding, MainRefreshViewModel>(),
    IRefreshDelegate {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?): BaseRefreshLayoutBinding {
        return BaseRefreshLayoutBinding.inflate(inflater)
    }

    override fun initData() {
    }
}