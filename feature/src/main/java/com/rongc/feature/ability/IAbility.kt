package com.rongc.feature.ability

import androidx.lifecycle.DefaultLifecycleObserver

interface IAbility : DefaultLifecycleObserver {
    /**
     * 注册时立即执行
     * 注册时#onCrete()并不一定
     */
//    fun onCreateImmediately(
//        host: IHost<*>, inflater: LayoutInflater, container: ViewGroup? = null
//    )
}