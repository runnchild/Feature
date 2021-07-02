package com.rongc.feature.ui.host

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

interface IHost {
    val host: Host
    val lifecycleOwner: LifecycleOwner

    fun bindView(inflater: LayoutInflater, container: ViewGroup? = null): ViewBinding? = null
}