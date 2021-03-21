package com.rongc.feature.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.rongc.feature.BR
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * 支持DataBinding的BaseFragment
 */
abstract class BaseBindingFragment<B : ViewDataBinding, M : BaseViewModel<out BaseModel>> : BaseFragment<M>(), IBinding<B> {

    protected lateinit var binding: B

    final override fun inflate(inflater: LayoutInflater, container: ViewGroup?): View {
        return binding(inflater, container).apply {
            binding = this
            binding.lifecycleOwner = this@BaseBindingFragment
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.ui, this)
        }.root
    }
}