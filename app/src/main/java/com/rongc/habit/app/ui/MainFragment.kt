package com.rongc.habit.app.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rongc.habit.app.R
import com.rongc.habit.app.databinding.MainFragmentBinding
import com.rongc.habit.ui.BaseBindingFragment
import com.rongc.habit.app.viewmodel.MainFragmentViewModel

class MainFragment : BaseBindingFragment<MainFragmentBinding, MainFragmentViewModel>() {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?): MainFragmentBinding {
        return MainFragmentBinding.inflate(inflater, container, false)
    }

    override fun viewClick(view: View) {
        when (view.id) {
            R.id.btn_viewBinding -> startActivity(Intent(view.context, ViewBindingAdapterActivity::class.java))
        }
    }
}