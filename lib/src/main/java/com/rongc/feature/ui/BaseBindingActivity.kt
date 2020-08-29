package com.rongc.feature.ui

import android.view.View
import androidx.databinding.ViewDataBinding
import com.rongc.feature.BR
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * 支持DataBinding的BaseActivity
 */
abstract class BaseBindingActivity<B: ViewDataBinding, M: BaseViewModel<out BaseModel>>: BaseActivity<M>(), IBinding<B> {

    protected lateinit var binding: B

    override fun getContentView(): View? {
        return binding(layoutInflater, null).apply {
            binding = this
            binding.lifecycleOwner = this@BaseBindingActivity
            binding.setVariable(BR.viewModel, viewModel)
        }.root
    }

    override fun getContentViewRes() = 0
}