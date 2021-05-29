package com.rongc.feature.ui.host

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.rongc.feature.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

interface IHost<M : BaseViewModel>: IAbilityHost {
    val host: Host
    val viewModel: M
    val lifecycleOwner: LifecycleOwner

    fun viewModelProvider(): M {
        var cls: Class<*> = this.javaClass

        var modelClass: Class<M>? = null
        while (modelClass == null) {
            @Suppress("UNCHECKED_CAST")
            modelClass = hasViewModelType(cls) as? Class<M>
            modelClass?:let {
                cls = cls.superclass as Class<*>
            }
        }

        return ViewModelProvider(this as ViewModelStoreOwner, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return viewModelCreator(modelClass as Class<M>)() as T
            }
        }).get(modelClass)
    }

    private fun hasViewModelType(cls: Class<*>): Type? {
        val type = cls.genericSuperclass
        return if (type is ParameterizedType) {
            type.actualTypeArguments.firstOrNull {
                BaseViewModel::class.java.isAssignableFrom(it as Class<*>)
            }
        } else null
    }

    fun viewModelCreator(cls: Class<M>): () -> M

}