package com.rongc.habit.app.ui

import android.view.View
import com.rongc.habit.app.R
import com.rongc.habit.ui.BaseActivity
import com.rongc.habit.app.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel>() {

    override fun getContentViewRes() = R.layout.activity_main

    override fun initView(view: View) {
        supportFragmentManager.beginTransaction().add(
            R.id.fragmentLayout,
            MainFragment(), ""
        ).commit()
    }
}