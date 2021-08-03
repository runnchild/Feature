package com.rongc.feature.ability.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.rongc.feature.AppExecutors
import com.rongc.feature.BR
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ui.host.IHost
import java.lang.reflect.ParameterizedType

/**
 * 自动创建ViewBinding/DataBinding的能力
 * 如果layout中定义了viewModel属性，则会自动为他赋值
 */
class BindingAbility<B : ViewBinding>(val viewModel: ViewModel) : IAbility {

    var mBinding: B? = null

    fun onCreateImmediately(
        host: IHost, inflater: LayoutInflater, container: ViewGroup? = null
    ) {
        val binding = (host.bindView(inflater, container) as B?) ?: binding(host, inflater, container)
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = host.lifecycleOwner
            try {
                binding::class.java.superclass?.getDeclaredField("mViewModel")
                // 如果xml中定义了名为viewModel属性, 为他赋值
                binding.setVariable(BR.viewModel, viewModel)
            } catch (e: Exception) {
            }
        }
        mBinding = binding
    }

    private fun binding(owner: IHost, inflater: LayoutInflater, container: ViewGroup?): B {
        val method = getBindingClass(owner).getDeclaredMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return (method.invoke(null, inflater, container, false) as B)
    }

    private fun getBindingClass(owner: IHost): Class<*> {
        var type = owner::class.java.genericSuperclass
        while (type is Class<*>) {
            type = type.genericSuperclass
        }
        return (type as ParameterizedType).actualTypeArguments[0] as Class<*>
    }

    /**
     * 页面销毁或者Fragment#onDestroyView执行时解除绑定
     */
    override fun onDestroy(owner: LifecycleOwner) {
        AppExecutors.mainThread().execute {
            val binding = mBinding
            if (binding is ViewDataBinding) {
                binding.unbind()
            }
            mBinding = null
        }
    }
}