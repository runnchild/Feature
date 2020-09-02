package com.rongc.feature.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rongc.feature.app.ui.viewmodel.MainRefreshViewModel
import com.rongc.feature.databinding.BaseRefreshLayoutBinding
import com.rongc.feature.ui.BaseBindingActivity
import com.rongc.feature.ui.IRefreshDelegate
import com.rongc.feature.ui.toolbar.BarConfig

class MainRefreshActivity : BaseBindingActivity<BaseRefreshLayoutBinding, MainRefreshViewModel>(),
    IRefreshDelegate {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?): BaseRefreshLayoutBinding {
        return BaseRefreshLayoutBinding.inflate(inflater)
    }

    override fun initData() {
    }

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            menu {
                text = "刷新"
                setOnClickListener {
                    viewModel.refresh()
                }
            }
            menu {
                text = "刷新2"
                setOnClickListener {
                    viewModel.refresh()
                }
            }
        }
    }
}