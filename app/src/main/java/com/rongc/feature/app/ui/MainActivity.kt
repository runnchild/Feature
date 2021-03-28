package com.rongc.feature.app.ui

import android.os.Bundle
import android.view.View
import com.rongc.feature.app.R
import com.rongc.feature.app.ui.viewmodel.MainViewModel
import com.rongc.feature.ui.BaseActivity
import com.rongc.feature.ui.toolbar.BarConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getContentViewRes() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
    override fun initView(view: View) {
        supportFragmentManager.beginTransaction().add(
            R.id.fragmentLayout,
            MainFragment(), ""
        ).commit()
    }

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            toolbarBackVisible = false
        }
    }
}