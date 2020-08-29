package com.rongc.feature.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

interface IBinding<B : ViewDataBinding> {
    /**
     * 返回页面布局的ViewBinding
     * @return 如果是Activity， 返回ActivityBinding.inflate(inflater),
     * 如果是Fragment 返回FragmentBinding.inflate(inflater, container, false)
     */
    fun binding(inflater: LayoutInflater, container: ViewGroup?): B
}