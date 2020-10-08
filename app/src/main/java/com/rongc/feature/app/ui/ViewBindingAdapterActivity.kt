package com.rongc.feature.app.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rongc.feature.app.databinding.ActivityViewBindingAdapterBinding
import com.rongc.feature.ui.BaseBindingActivity
import com.rongc.feature.utils.ClickSpanEt.setClickSpans
import com.rongc.feature.utils.Compat.toast
import com.rongc.feature.viewmodel.EmptyViewModel

class ViewBindingAdapterActivity :
    BaseBindingActivity<ActivityViewBindingAdapterBinding, EmptyViewModel>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityViewBindingAdapterBinding {
        return ActivityViewBindingAdapterBinding.inflate(inflater)
    }

    override fun initView(view: View) {
        super.initView(view)
        binding.tvAgreement.setClickSpans(Color.BLUE,{
            "用户协议".toast()
        }, {
            "隐私政策".toast()
        })
    }
}