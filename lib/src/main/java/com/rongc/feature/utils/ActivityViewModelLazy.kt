package com.rongc.feature.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.rongc.feature.ui.BaseActivity
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * @description 创建继承BaseViewModel的ViewModel， 当前所在的Activity必须继承BaseActivity
 *
 * @author rongc
 * @date 2020/8/31$
 * @update
 */
object ActivityViewModelLazy {
    @MainThread
    inline fun <reified VM : BaseViewModel<*>> Fragment.activityViewModel(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ) =  createViewModelLazy(VM::class, { requireActivity().viewModelStore },
        factoryProducer ?: {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(clz: Class<T>): T {
                    return (requireActivity() as BaseActivity<*>).obtainSubViewModel(clz)
                }
            }
        })

    @MainThread
    inline fun <reified VM : BaseViewModel<*>> BaseActivity<*>.activityViewModel(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return obtainSubViewModel(modelClass)
            }
        }
        return ViewModelLazy(VM::class, { viewModelStore }, factoryProducer ?: { factory })
    }
}