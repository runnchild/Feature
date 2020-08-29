package com.rongc.feature.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.rongc.feature.app.databinding.ActivityViewBindingAdapterBinding
import com.rongc.feature.ui.BaseBindingActivity
import com.rongc.feature.viewmodel.EmptyViewModel

class ViewBindingAdapterActivity :
    BaseBindingActivity<ActivityViewBindingAdapterBinding, EmptyViewModel>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityViewBindingAdapterBinding {
        return ActivityViewBindingAdapterBinding.inflate(inflater)
    }
}