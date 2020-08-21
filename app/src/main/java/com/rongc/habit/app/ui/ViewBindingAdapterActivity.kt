package com.rongc.habit.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rongc.habit.app.databinding.ActivityViewBindingAdapterBinding
import com.rongc.habit.ui.BaseBindingActivity
import com.rongc.habit.viewmodel.EmptyViewModel

class ViewBindingAdapterActivity :
    BaseBindingActivity<ActivityViewBindingAdapterBinding, EmptyViewModel>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityViewBindingAdapterBinding {
        return ActivityViewBindingAdapterBinding.inflate(inflater)
    }
}