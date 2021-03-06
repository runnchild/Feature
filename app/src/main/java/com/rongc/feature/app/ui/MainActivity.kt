package com.rongc.feature.app.ui

import android.view.View
import com.rongc.feature.app.R
import com.rongc.feature.app.ui.viewmodel.MainViewModel
import com.rongc.feature.ui.BaseActivity
import com.rongc.feature.ui.toolbar.BarConfig

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getContentViewRes() = R.layout.activity_main

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