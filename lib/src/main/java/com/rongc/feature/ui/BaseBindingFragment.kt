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
 * 支持DataBinding的BaseFragment
 */
abstract class BaseBindingFragment<B : ViewDataBinding, M : BaseViewModel<out BaseModel>> :
    BaseFragment<M>(), IBinding<B> {

    protected lateinit var binding: B

    final override fun inflate(inflater: LayoutInflater, container: ViewGroup?): View {
        return binding(inflater, container).apply {
            binding = this
            binding.lifecycleOwner = this@BaseBindingFragment
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.ui, this)
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
}