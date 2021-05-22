package com.runnchild.feature.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

interface IUI<B: ViewBinding> {

    open fun binding(inflater: LayoutInflater, container: ViewGroup?): B {
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