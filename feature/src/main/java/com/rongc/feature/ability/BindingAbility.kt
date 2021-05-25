package com.rongc.feature.ability

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.rongc.feature.BR
import com.rongc.feature.ui.host.IHost
import java.lang.reflect.ParameterizedType

class BindingAbility<B : ViewBinding> : IAbility {

    var mBinding: B? = null

    fun onCreateImmediately(
        host: IHost<*>,
        inflater: LayoutInflater,
        container: ViewGroup? = null
    ) {
        val binding = binding(host, inflater, container)
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = host.lifecycleOwner
            try {
                // 如果xml中定义了viewModel, 赋值
                binding::class.java.superclass?.getDeclaredField("mViewModel")
                binding.setVariable(BR.viewModel, host.viewModel)
            } catch (e: Exception) {
            }
        }
        mBinding = binding
    }

    private fun binding(owner: IHost<*>, inflater: LayoutInflater, container: ViewGroup?): B {
        val bindingClass =
            (owner::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
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

    override fun onDestroy(owner: LifecycleOwner) {
        val binding = mBinding
        if (binding is ViewDataBinding) {
            binding.unbind()
        }
        mBinding = null
    }
}