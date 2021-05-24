package com.rongc.feature.ability

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface IAbility : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
    }
}