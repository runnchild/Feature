package com.rongc.habit.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rongc.habit.databinding.BaseRefreshLayoutBinding
import com.rongc.habit.ui.BaseBindingActivity
import com.rongc.habit.ui.IRefreshDelegate
import com.rongc.habit.app.viewmodel.MainRefreshViewModel

class MainRefreshActivity : BaseBindingActivity<BaseRefreshLayoutBinding, MainRefreshViewModel>(),
    IRefreshDelegate {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?): BaseRefreshLayoutBinding {
        return BaseRefreshLayoutBinding.inflate(inflater)
    }

    override fun initData() {
    }
}