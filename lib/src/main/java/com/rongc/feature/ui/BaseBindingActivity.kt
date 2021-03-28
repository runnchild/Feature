package com.rongc.feature.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.rongc.feature.BR
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

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

    override fun binding(inflater: LayoutInflater, container: ViewGroup?): B {
        val bindingClass =
            (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = bindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return (method.invoke(null, inflater, container, false) as B)
    }

    override fun getContentViewRes() = 0
}