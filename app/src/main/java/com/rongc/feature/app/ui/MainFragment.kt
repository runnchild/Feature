package com.rongc.feature.app.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rongc.feature.app.R
import com.rongc.feature.app.databinding.MainFragmentBinding
import com.rongc.feature.app.ui.viewmodel.MainFragmentViewModel
import com.rongc.feature.ui.BaseBindingFragment
import com.rongc.feature.ui.toolbar.BarConfig

class MainFragment : BaseBindingFragment<MainFragmentBinding, MainFragmentViewModel>() {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?): MainFragmentBinding {
        return MainFragmentBinding.inflate(inflater, container, false)
    }

    override fun viewClick(view: View) {
        when (view.id) {
            R.id.btn_viewBinding -> startActivity(Intent(view.context, ViewBindingAdapterActivity::class.java))
            R.id.btn_viewPager2-> startActivity(Intent(view.context, ViewPager2Activity::class.java))
            R.id.btn_status_bar-> startActivity(Intent(view.context, StatusBarActivity::class.java))
        }
    }

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            toolbarVisible = false
        }
    }
}